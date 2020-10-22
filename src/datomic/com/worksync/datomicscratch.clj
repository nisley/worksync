(ns com.worksync.datomicscratch
  (:require
    [taoensso.timbre :as log]))

(require '[datomic.client.api :as d])

(def cfg {:server-type :dev-local
          :system      "fulcro-rad-demo"})

(def client (d/client cfg))
(def conn (d/connect client {:db-name "example"}))
(def db (d/db conn))

(def all-lists '[:find ?abc
                 :where [_ :list/id ?abc]])
(d/q all-lists db)


(d/q
  '[:find ?id ?cat
    :where
    [?e :item/id ?id]
    [?e :item/category ?cat]]
  db)

(d/q '[:find ?uuid
       :in $ ?catid
       :where
       [?c :category/id ?catid]
       [?i :item/category ?c]
       [?i :item/id ?uuid]] db #uuid"ffffffff-ffff-ffff-ffff-000000000206")

(d/q
  '[:find ?id ?last
    :where
    [?e :assignee/id ?id]
    [?e :assignee/lastname ?last]]
  db)

(d/q
  '[:find ?id ?title
    :where
    [?e :list/id ?id]
    [?e :list/title ?title]]
  db)

;;Finally proves the data is in there
(d/q '[:find ?lid
       :in $ ?list-id
       :where
       [?e :list/id ?list-id]
       [?e :list/assignee ?ass]
       [?ass :assignee/id ?lid]] db #uuid"14e46b95-3d8f-4f11-a473-bdadb26ace84")

(d/q '[:find ?uuid
       :in $ ?assid
       :where
       [?a :assignee/id ?assid]
       [?list :list/title ?a]
       [?list :list/id ?uuid]] db 10002)
#_
(d/q
  '[:find ?line-item-id
    :where
    [?e :line-item/id ?line-item-id]]
  db)

#_
(d/q '[:find ?cid
       :in $ ?line-item-id
       :where
       [?e :line-item/id ?line-item-id]
       [?e :line-item/item ?item]
       [?item :item/category ?c]
       [?c :category/id ?cid]] db #uuid"6bab76c4-9dac-4100-b7f9-a9c8e94f88d9")

#_
(d/q
  '[:find ?id ?ass ?lastname
    :where
    [?e :list/id ?id]
    [?e :list/assignee ?ass]
    [?ass :assignee/id ?assid]
    [?assid :assignee/lastname ?lastname]]

  db)


