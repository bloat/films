(ns films.core
  (require [pl.danieljanus.tagsoup :as ts]
           [hiccup.page :as hp]))

(defn find-body [html]
  (some #(when (vector? %)
           (let [tag (first %)]
             (when (= :body tag)
               %))) html))

(defn get-channel [film]
  (some #(when (= "chanbox" (get-in % [1 :class]))
           (if (= :a (get-in % [2 0]))
             (get-in % [2 5 2])
             (get-in % [5 2]))) film))

(defn get-title [film]
  (some #(when (= "title" (get-in % [1 :class]))
           (get-in % [2 2])) film))

(defn get-info [film]
  (some #(when (= "panel" (get-in % [1 :class]))
           (last (get-in % [2 2 2]))) film))

(defn get-info2 [film]
  (some #(when (= "panel" (get-in % [1 :class]))
           (get-in % [2 2 2 2])) film))

(defn get-time [film]
  (some #(when (= "time" (get-in % [1 :class]))
           (get-in % [2 2])) film))

(def url "http://www.nextfilm.co.uk/index.php")

(def urls (conj (map #(str url "?id=" %) [1 2 3 4 5 6 7 8 9]) url))

(defn find-films [body]
  (filter #(not (contains? #{"Movies4Men" "True Entertainment"} (get-channel %)))
          (filter #(and (vector? %)
                        (= "listentry" (get-in % [1 :class])))
                  (get-in body [2 3 3]))))

(defn all-films []
  (mapcat #(map (juxt get-title get-info) (find-films (find-body (ts/parse %)))) urls))

(defn all-films-html [films]
  (hp/html5 [:head] [:body [:table {:border "2"} (for [[title desc] films] [:tr [:td title] [:td desc]])]]))
(defn -main []
(spit "/Users/bloat/films.html" (all-films-html (all-films))))
