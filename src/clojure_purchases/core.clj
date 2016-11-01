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
     [:tr
      [:td
       [:form {:action "/category" :method "get"}
        [:input {:type "hidden" :value "Alcohol"}]
        [:button {:type "submit" :value "Alcohol"}]]]
      [:td
       [:form {:action "/category" :method "get"}
        [:input {:type "hidden" :value "Food"}]
        [:button {:type "submit" :value "Food"}]]]
      [:td
       [:form {:action "/category" :method "get"}
        [:input {:type "hidden" :value "Furniture"}]
        [:button {:type "submit" :value "Furniture"}]]]
      [:td
       [:form {:action "/category" :method "get"}
        [:input {:type "hidden" :value "Jewelry"}]
        [:button {:type "submit" :value "Jewelry"}]]]
      [:td
       [:form {:action "/category" :method "get"}
        [:input {:type "hidden" :value "Shoes"}]
        [:button {:type "submit" :value "Shoes"}]]]
      [:td
       [:form {:action "/category" :method "get"}
        [:input {:type "hidden" :value "Toiletries"}]
        [:button {:type "submit" :value "Toiletries"}]]]]]
    
    
    [:table {:width 800 :border 1}
     [:th "Customer id"]
     [:th "Purchase date"]
     [:th "CC Number"]
     [:th "CVV Number"]
     [:th "Purchase Category"]
     [:tr
      [:td
       [:a {:href "/:Alcohol"} "Alcohol"]]
      [:td
       [:a {:href "/:Food"} "Food"]]
      [:td
       [:a {:href "/:Furniture"} "Furniture"]]
      [:td
       [:a {:href "/:Jewelry"} "Jewelry"]]
      [:td
       [:a {:href "/:Shoes"} "Shoes"]]
      [:td
       [:a {:href "/:Toiletries"} "Toiletries"]]]
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
