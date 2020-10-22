(ns com.worksync.components.save-middleware
  (:require
    [com.fulcrologic.rad.middleware.save-middleware :as r.s.middleware]
    [com.fulcrologic.rad.database-adapters.datomic-cloud :as datomic]
    [com.worksync.components.datomic :refer [datomic-connections]]
    [com.fulcrologic.rad.blob :as blob]
    [com.worksync.model :as model]))

(def middleware
  (->
    (datomic/wrap-datomic-save)
    (blob/wrap-persist-images model/all-attributes)
    (r.s.middleware/wrap-rewrite-values)))
