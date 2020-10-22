(ns com.worksync.ui.assignee-forms
  (:require
    [com.worksync.model.assignee :as assignee]
    [com.fulcrologic.rad.picker-options :as picker-options]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.rad.control :as control]
    [com.fulcrologic.rad.form :as form]
    [com.fulcrologic.rad.form-options :as fo]
    [com.fulcrologic.rad.report :as report]
    [com.fulcrologic.rad.report-options :as ro]
    [taoensso.timbre :as log]))



(report/defsc-report AssigneeReport [this props]
  {ro/title               "Assignee Report"
   ro/source-attribute    :assignee/all-assignees
   ro/row-pk              assignee/id
   ro/columns             [assignee/lastname]
   ro/run-on-mount?       true
   ro/route               "assignee-report"})
