(ns com.worksync.components.datomic
  (:require
    [com.fulcrologic.rad.database-adapters.datomic-cloud :as datomic]
    [mount.core :refer [defstate]]
    [com.worksync.model :refer [all-attributes]]
    [com.worksync.components.config :refer [config]]))

(defstate ^{:on-reload :noop} datomic-connections
  :start
  (datomic/start-databases all-attributes config))
