(ns breaking-bad-quotes.core
  (:require [reagent.core :as r :refer [atom]]
            [ajax.core :refer [GET]]
            [breaking-bad-quotes.fb.init :refer [firebase-init]]))

(defn fetch-link! [link]
  (GET "https://breaking-bad-quotes.herokuapp.com/v1/quotes"
    {:handler #(reset! link %)
     :error-handler (fn [{:keys [status status-text]}] (js/console.log status status-text))}))

(defn quote []
  (let [link (atom nil)]
    (fetch-link! link)
    (fn []
      (let [{:strs [quote author]} (first @link)
            tweet-intent (str "https://twitter.com/intent/tweet?hashtags=breakingbad&text=" quote " ~ " author)]
        [:div.cards>div.card
         [:h2.card-header.text-center "Breaking Bad Quotes"]
         [:div.card-body.text-center
          [:p#quote quote]
          [:p#author author]]
         [:div.card-footer.center.text-center
          [:button#twitter.outline>a#tweet
           {:href tweet-intent
            :target "_blank"}
           [:i.fi-social-twitter " Tweet"]]
          [:button#new-quote.outline
           {:on-click #(fetch-link! link)}
           [:i.fi-shuffle " New Quote"]]]]))))


(defn start []
  (r/render-component [quote]
                      (. js/document (getElementById "app"))))

(defn ^:export init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (start)
  (firebase-init))

(defn stop []
  ;; stop is called before any code is reloaded
  ;; this is controlled by :before-load in the config
  (js/console.log "stop"))