(ns com.worksync.ui.list-forms
  (:require
    [com.worksync.model.list :as list]
    [com.fulcrologic.rad.picker-options :as picker-options]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.rad.control :as control]
    [com.fulcrologic.rad.form :as form]
    [com.fulcrologic.rad.form-options :as fo]
    [com.fulcrologic.rad.report :as report]
    [com.fulcrologic.rad.report-options :as ro]
    [com.worksync.model.assignee :as assignee]
    [taoensso.timbre :as log]))



(form/defsc-form ListForm [this props]
  {fo/id            list/id
   fo/attributes    [list/title
                     list/assignee]
   fo/route-prefix  "list"
   fo/title         "List of TODOsn"
   fo/field-styles  {:list/assignee :pick-one}
   fo/field-options {:list/assignee {::picker-options/query-key       :assignee/all-assignees
                                     ::picker-options/query-component assignee/Assignee
                                     ::picker-options/options-xform   (fn [_ options] (mapv
                                                                                        (fn [{:assignee/keys [id lastname]}]
                                                                                          {:text (str lastname) :value [:assignee/id id]})
                                                                                        (sort-by :assignee/lastname options)))
                                     ::picker-options/cache-time-ms   30000}}})


(report/defsc-report ListReport [this props]
  {ro/title               "List Report"
   ro/source-attribute    :list/all-lists
   ro/row-pk              list/id
   ro/columns             [list/title assignee/lastname list/id]
   ro/form-links          {list/title ListForm}

   ro/run-on-mount?       true
   ro/route               "list-report"})