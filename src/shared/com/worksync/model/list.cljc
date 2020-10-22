(ns com.worksync.model.list
  (:require
    [com.fulcrologic.rad.attributes :as attr :refer [defattr]]
    [com.fulcrologic.rad.attributes-options :as ao]
    [com.wsscode.pathom.connect :as pc]
    #?(:clj [com.worksync.components.database-queries :as queries])
    [taoensso.timbre :as log]))

(defattr id :list/id :uuid
  {ao/identity? true
   ao/schema    :production})

(defattr assignee :list/assignee :ref
  {ao/target :assignee/id
   ao/cardinality :one
   ao/identities #{:list/id}
   ao/schema :production})

(defattr title :list/title :string
  {ao/identities #{:list/id}
   ao/schema     :production})


(defattr all-lists :list/all-lists :ref
  {ao/target    :list/id
   ::pc/output  [{:list/all-lists [:list/id]}]
   ::pc/resolve (fn [{:keys [query-params] :as env} _]
                  #?(:clj
                     {:list/all-lists (queries/get-all-lists env (log/spy :info query-params))}))})

#?(:clj
   (pc/defresolver list-assignee-resolver [{:keys [parser] :as env} {:list/keys [id]}]
     {::pc/input  #{:list/id}
      ::pc/output [:assignee/id :assignee/lastname]}
     (let [result (parser env [{[:list/id id] [{:list/assignee [:assignee/id :assignee/lastname]}]}])]
       (get-in (log/spy :info result) [[:list/id id] :list/assignee]))))

(def attributes [id title assignee all-lists])

#?(:clj
   (def resolvers [list-assignee-resolver]))

