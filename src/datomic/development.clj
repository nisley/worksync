(ns development
  (:require
    [clojure.pprint :refer [pprint]]
    [clojure.repl :refer [doc source]]
    [clojure.tools.namespace.repl :as tools-ns :refer [disable-reload! refresh clear set-refresh-dirs]]
    [com.worksync.components.datomic :refer [datomic-connections]]
    [com.worksync.components.ring-middleware]
    [com.worksync.components.server]
    [com.worksync.model.seed :as seed]
    [com.worksync.model.account :as account]
    [com.worksync.model.address :as address]
    [com.fulcrologic.rad.ids :refer [new-uuid]]
    [com.fulcrologic.rad.database-adapters.datomic-cloud :as datomic]
    [com.fulcrologic.rad.resolvers :as res]
    [mount.core :as mount]
    [taoensso.timbre :as log]
    [datomic.client.api :as d]
    [com.fulcrologic.rad.attributes :as attr]
    [com.fulcrologic.rad.type-support.date-time :as dt]))

(set-refresh-dirs "src/main" "src/datomic" "src/dev" "src/shared")

(comment
  (let [db (d/db (:main datomic-connections))]
    (d/pull db '[*] [:account/id (new-uuid 100)])))

(defn seed! []
  (dt/set-timezone! "America/Los_Angeles")
  (let [connection (:main datomic-connections)
        date-1 (dt/html-datetime-string->inst "2020-01-01T12:00")
        date-2 (dt/html-datetime-string->inst "2020-01-05T12:00")
        date-3 (dt/html-datetime-string->inst "2020-02-01T12:00")
        date-4 (dt/html-datetime-string->inst "2020-03-10T12:00")
        date-5 (dt/html-datetime-string->inst "2020-03-21T12:00")]
    (when connection
      (log/info "SEEDING data.")
      (d/transact connection {:tx-data [(seed/new-address (new-uuid 1) "111 Main St.")
                                        (seed/new-account (new-uuid 100) "Tony" "tony@example.com" "letmein"
                                          :account/addresses ["111 Main St."]
                                          :account/primary-address (seed/new-address (new-uuid 300) "222 Other")
                                          :time-zone/zone-id :time-zone.zone-id/America-Los_Angeles)
                                        (seed/new-account (new-uuid 101) "Sam" "sam@example.com" "letmein")
                                        (seed/new-account (new-uuid 102) "Sally" "sally@example.com" "letmein")
                                        (seed/new-account (new-uuid 103) "Barbara" "barb@example.com" "letmein")
                                        (seed/new-category (new-uuid 1000) "Tools")
                                        (seed/new-category (new-uuid 1002) "Toys")
                                        (seed/new-category (new-uuid 1003) "Misc")
                                        (seed/new-assignee (new-uuid 10000) "Ross")
                                        (seed/new-assignee (new-uuid 10001) "Finklebime")
                                        (seed/new-assignee (new-uuid 10002) "Finau")
                                        (seed/new-item (new-uuid 200) "Widget" 33.99
                                          :item/category "Misc")
                                        (seed/new-item (new-uuid 201) "Screwdriver" 4.99
                                          :item/category "Tools")
                                        (seed/new-item (new-uuid 202) "Wrench" 14.99
                                          :item/category "Tools")
                                        (seed/new-item (new-uuid 203) "Hammer" 14.99
                                          :item/category "Tools")
                                        (seed/new-item (new-uuid 204) "Doll" 4.99
                                          :item/category "Toys")
                                        (seed/new-item (new-uuid 205) "Robot" 94.99
                                          :item/category "Toys")
                                        (seed/new-item (new-uuid 206) "Building Blocks" 24.99
                                          :item/category "Toys")]}))))

(defn start []
  (mount/start-with-args {:config "config/dev.edn"})
  (seed!)
  :ok)

(defn stop
  "Stop the server."
  []
  (mount/stop))

(def go start)

(defn restart
  "Stop, refresh, and restart the server."
  []
  (stop)
  (tools-ns/refresh :after 'development/start))

(def reset #'restart)

(comment
  (start)
  (restart))
