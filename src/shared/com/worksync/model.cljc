(ns com.worksync.model
  (:require
    [com.worksync.model.timezone :as timezone]
    [com.worksync.model.account :as account]
    [com.worksync.model.item :as item]
    [com.worksync.model.invoice :as invoice]
    [com.worksync.model.address :as address]
    [com.worksync.model.category :as category]
    [com.worksync.model.list :as list]
    [com.worksync.model.assignee :as assignee]
    [com.worksync.model.file :as m.file]
    [com.worksync.model.sales :as sales]
    [com.fulcrologic.rad.attributes :as attr]))

(def all-attributes (vec (concat
                           account/attributes
                           address/attributes
                           category/attributes
                           assignee/attributes
                           item/attributes
                           list/attributes
                           invoice/attributes
                           m.file/attributes
                           sales/attributes
                           timezone/attributes)))

(def all-attribute-validator (attr/make-attribute-validator all-attributes))
