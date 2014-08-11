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
  (->>
    (map #(str/replace-first % #"(?i)java\s.*" "Java") technologies)
    (map #(str/replace-first % #"(?i)html.*" "HTML"))))

(defn filter-technologies [technologies]
  (distinct (find-similar technologies)))

(defn output [filename map]
  (spit filename (json/write-str {:name "Tech" :children map})))

(defn get-tech-from-cv [url]
  (->
    (get-file url)
    (extract-technologies)))

(defn get-tech-from-last-project [url]
  (->>
    (get-file url)
    (:project_experiences)
    (sort-by :order)
    (first)
    (:local_tags)))

(defn format-and-spit [filename keywords-vector]
  (->>
    (sort keywords-vector)
    (frequencies)
    (map (fn [tech] {:name (first tech) :size (second tech)}))
    (output filename)))

(defn go []
  (->>
    (get-cv-urls)
    (mapcat #(filter-technologies (get-tech-from-cv %)))
    (format-and-spit "techs.json")))

(defn go-last-project []
  (->>
    (get-cv-urls)
    (mapcat #(get-tech-from-last-project %))
    (format-and-spit "current-tech.json")))


