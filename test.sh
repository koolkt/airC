token=$(curl -X POST -H "Content-type: application/json" -d '{"username": "jdixon3@deliciousdays.com", "password": "Y5ya4XYXA0v"}' http://localhost:3000/authenticate -i | grep 'X-Auth-Token' | cut -d : -f 2)

auth="X-Auth-Token: ${token:0:-1}"

echo -e '\n'
curl  'http://localhost:3000/api/v1/hello-user' -H "Accept: application/json" -H "$auth"
echo -e '\n'
curl 'http://localhost:3000/api/v1/name' -H "Accept: application/json" -H "$auth"
echo -e '\n'
curl 'http://localhost:3000/api/v1/hello-admin' -H "Accept: application/json" -H "$auth"
echo -e '\n'
