(ns com.worksync.model.assignee
  (:require
    [com.fulcrologic.rad.attributes :as attr :refer [defattr]]
    [com.fulcrologic.rad.authorization :as auth]
    [com.fulcrologic.rad.attributes-options :as ao]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.wsscode.pathom.connect :as pc]
    #?(:clj [com.worksync.components.database-queries :as queries])))

(defsc Assignee [_ _]
  {:query [:assignee/id :assignee/lastname]
   :ident :assignee/id})


(defattr id :assignee/id :uuid
  {ao/identity? true
   ao/schema    :production})

(defattr lastname :assignee/lastname :string
  {ao/required?                                      true
   ao/identities                                     #{:assignee/id}
   :com.fulcrologic.rad.database-adapters.sql/max-length 120
   ao/schema                                         :production})

(defattr all-assignees :assignee/all-assignees :ref
  {ao/target :assignee/id
   ao/pc-output   [{:assignee/all-assignees [:assignee/id]}]
   ao/pc-resolve  (fn [{:keys [query-params] :as env} _]
                   #?(:clj
                      {:assignee/all-assignees (queries/get-all-assignees env query-params)}))})

(def attributes [id lastname all-assignees])

