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
     [:th {:colspan 5}
       [:a {:href "/"} "All"]
       (repeat 10 "&nbsp")
       [:a {:href "/Alcohol"} "Alcohol"]
       (repeat 10 "&nbsp ")
       [:a {:href "/Food"} "Food"]
       (repeat 10 "&nbsp")
       [:a {:href "/Furniture"} "Furniture"]
       (repeat 10 "&nbsp")
       [:a {:href "/Jewelry"} "Jewelry"]
       (repeat 10 "&nbsp")
       [:a {:href "/Shoes"} "Shoes"]
       (repeat 10 "&nbsp")
       [:a {:href "/Toiletries"} "Toiletries"]]
     [:tr
      [:td "Customer id"]
      [:td "Purchase date"]
      [:td "CC Number"]
      [:td "CVV Number"]
      [:td "Purchase Category"]] 
     (map (fn [purchase] purchases
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
                     (get purchase "category"))]])
       purchases)]))



(c/defroutes app
  (c/GET "/" []
    (h/html [:html [:body [:a {:href "/:category=Furniture"}] (purchases-html nil)]]))
  (c/GET "/:category" [category]
    (h/html [:html [:body (purchases-html category)]])))

(defn -main [& args]
  (j/run-jetty app {:port 3000}))
