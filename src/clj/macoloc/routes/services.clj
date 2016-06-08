(ns macoloc.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [macoloc.db.core :as db]
            [schema.core :as s]
            [macoloc.auth :as friend-jwt]
            [cemerick.friend :as friend]
            [cemerick.friend.credentials :as creds]
            [cemerick.friend.util :refer [gets]]
            [clj-jwt.key :refer [public-key private-key]]
            [clj-jwt.intdate :refer [intdate->joda-time]]))

(s/defschema Bounds {:ne {:lat Double :lng Double}
                     :sw {:lat Double :lng Double}})

(s/defschema User {:email s/Str
                   :first_name s/Str
                   :age Integer
                   :admin s/Str
                   :profile_picture s/Str
                   :phone s/Str
                   :ip_address s/Str
                   :last_login s/Any
                   :is_active s/Str
                   :id Integer
                   :last_name s/Str
                   :pass s/Str
                   :gender s/Str})

(defapi service-routes-vanilla
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Sample API"
                           :description "Sample Services"}}}}
  (context "/api/v1" []
           :tags ["colocs"]

           (GET "/users" []
                :return       User
                :query-params [id :- Long]
                :summary      "Get users by id"
                (-> (ok (db/get-user {:id id}))
                    (content-type "application/json")))

            (GET "/hello-user" []
                 :return       s/Str
                 :summary      "Get users by id"
                 (-> (friend/authorize #{:macoloc.auth/user} (ok "Hello authorized user"))
                     (content-type "application/json")))

            (GET "/hello-admin" []
                 :return       s/Str
                 :summary      "Get users by id"
                 (-> (friend/authorize #{:macoloc.auth/admin} (ok "Hello authorized admin"))
                     (content-type "application/json")))

            (GET "/name" []
                 :return s/Str
                 :summary "Get name"
                 :headers [h s/Any]
                 (friend/authenticated (str "Authenticated: Hello to you "
                                            (:username (friend/current-authentication))
                                            ", my good friend!!\n")))))

(def service-routes (friend/authenticate
                   service-routes-vanilla
                   {:allow-anon? true
                    :unauthenticated-handler friend-jwt/workflow-deny
                    :login-uri "/authenticate"
                    :workflows [friend-jwt/workflow]}))
