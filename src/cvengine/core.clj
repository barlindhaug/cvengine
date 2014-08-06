(ns cvengine.core
  (:gen-class)
  (:require
    [clj-http.client :as client]
    [clojure.data.json :as json]
    [clojure.string :as str]))

(def base-url "https://iterate.cvpartner.no/api/v1/cvs/")
(def users-url "https://iterate.cvpartner.no/api/v1/users")

(defn token-key []
  (str/trim-newline (slurp "token")))

(defn get-file [url]
  (:body (client/get url {:headers {"Authorization" (str "Token token=" (token-key))}
                          :as :json})))

(defn get-cv-urls []
  (->> (get-file users-url)
      (map (fn [user] (str base-url (:user_id user) "/" (:default_cv_id user))))))

(defn extract-technologies [cv]
  (mapcat (fn [category] (:tags category)) (:technologies cv)))

(defn find-similar [technologies]
  (map #(str/replace-first % #"(?i)java.*" "Java") technologies))

(defn filter-technologies [technologies]
  (distinct (find-similar technologies)))

(defn output [map]
  (spit "techs.json" (json/write-str {:name "Tech" :children map})))

(defn get-tech-from-cv [url]
  (->
    (get-file url)
    (extract-technologies)))

(defn go []
  (->>
    (get-cv-urls)
    (mapcat #(filter-technologies (get-tech-from-cv %)))
    (sort)
    (frequencies)
    (map (fn [tech] {:name (first tech) :size (second tech)}))
    (output)))




