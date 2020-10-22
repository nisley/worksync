(ns com.worksync.components.parser
  (:require
    [com.worksync.components.auto-resolvers :refer [automatic-resolvers]]
    [com.worksync.components.blob-store :as bs]
    [com.worksync.components.config :refer [config]]
    [com.worksync.components.datomic :refer [datomic-connections]]
    [com.worksync.components.delete-middleware :as delete]
    [com.worksync.components.save-middleware :as save]
    [com.worksync.model :refer [all-attributes]]
    [com.worksync.model.account :as account]
    [com.worksync.model.invoice :as invoice]
    [com.worksync.model.timezone :as timezone]
    [com.fulcrologic.rad.attributes :as attr]
    [com.fulcrologic.rad.blob :as blob]
    [com.fulcrologic.rad.database-adapters.datomic-cloud :as datomic]
    [com.fulcrologic.rad.form :as form]
    [com.fulcrologic.rad.pathom :as pathom]
    [mount.core :refer [defstate]]
    [com.worksync.model.sales :as sales]
    [com.worksync.model.item :as item]
    [com.worksync.model.list :as list]
    [com.wsscode.pathom.core :as p]
    [com.fulcrologic.rad.type-support.date-time :as dt]))

(defstate parser
  :start
  (pathom/new-parser config
    [(attr/pathom-plugin all-attributes)
     (form/pathom-plugin save/middleware delete/middleware)
     (datomic/pathom-plugin (fn [env] {:production (:main datomic-connections)}))
     (blob/pathom-plugin bs/temporary-blob-store {:files         bs/file-blob-store
                                                  :avatar-images bs/image-blob-store})
     {::p/wrap-parser
      (fn transform-parser-out-plugin-external [parser]
        (fn transform-parser-out-plugin-internal [env tx]
          ;; TASK: This should be taken from account-based setting
          (dt/with-timezone "America/Los_Angeles"
            (if (and (map? env) (seq tx))
              (parser env tx)
              {}))))}]
    [automatic-resolvers
     form/resolvers
     (blob/resolvers all-attributes)
     account/resolvers
     invoice/resolvers
     item/resolvers
     list/resolvers
     sales/resolvers
     timezone/resolvers]))
