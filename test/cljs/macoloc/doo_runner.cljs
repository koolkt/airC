(ns macoloc.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [macoloc.core-test]))

(doo-tests 'macoloc.core-test)

