(ns clojure-purchases.core
  (:require [clojure.string :as str]
            [compojure.core :as c]
            [ring.adapter.jetty :as j]
            [hiccup.core :as h])
  (:gen-class))

;        category (read-line)
;        purchases (filter (fn [line]
;                            (= (get line "category")
;                               category
;                    purchases)
;        purchases (pr-str purchases)]
;    (println purchases)
;    (spit "filtered_purchases.edn" purchases)))


(def file-name "purchases.csv")
;(def prompt (println "Enter a category"
;                 (println (def choice read-line))))
;(def choice (read-line))

(defn read-purchases []
  (let [purchases (str/split-lines (slurp file-name))
        purchases (map (fn [line]
                         (str/split line #","))
                    purchases)
        header (first purchases)
        purchases (rest purchases)
        purchases (map (fn [line]
                         (zipmap header line))
                    purchases)]
    purchases))
;customer_id,date,credit_card,cvv,category
(defn purchases-html [category]
  (let [purchases (read-purchases)
        purchases (filter (fn [purchase]
                            (or (nil? category)
                                (= category (get purchase "category"))))
                    purchases)]
    [:table {:width 800 :border 1}
     [:th "Customer id"]
     [:th "Purchase date"]
     [:th "CC Number"]
     [:th "CVV Number"]
     [:th "Purchase Category"]
     (map fn [purchase] purchases
             [:tr
              [:td (str
                     (get purchase "customer_id"))]
              [:td (str
                     (get purchase "date"))]
              [:td (str
                     (get purchase "credit_card"))]
              [:td (str
                     (get purchase "cvv"))]
              [:td (str
                     (get purchase "category"))]]
             purchases)]))



(c/defroutes app
  (c/GET "/" []
    (h/html [:html [:body (purchases-html nil)]]))
  (c/GET "/:category" [category]
    (h/html [:html [:body (purchases-html category)]])))

(defn -main [& args]
  (j/run-jetty app {:port 3000}))
