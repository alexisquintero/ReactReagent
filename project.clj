(defproject tac "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [reagent "0.7.0"]
                 [com.bhauman/figwheel-main "0.2.0"]
                 [com.bhauman/rebel-readline-cljs "0.1.4"]]

  :aliases {"fig" ["trampoline" "run" "-m" "figwheel.main"]
                      "build-dev" ["trampoline" "run" "-m" "figwheel.main" "-b" "dev" "-r"]}

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :resource-paths ["target" "resources"]

  :plugins [[lein-cljsbuild "1.1.4"]
            [lein-figwheel "0.5.0-1"]]

  :clean-targets ^{:protect false} ["resources/public/js"
                                    "target"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :profiles
  {:dev
   {:dependencies [[com.bhauman/figwheel-main "0.1.5"]
                   [com.bhauman/rebel-readline-cljs "0.1.4"]
                   [figwheel-sidecar "0.5.16"]]
    :source-paths ["src/cljs"]

    :plugins      [[lein-figwheel "0.5.15"]]
    }}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     true;{:on-jsload "tac.core/reload"}
     :compiler     {:main                 tac.core
                    :optimizations        :none
                    :output-to            "resources/public/js/app.js"
                    :output-dir           "resources/public/js/dev"
                    :asset-path           "js/dev"
                    :source-map-timestamp true}}

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            tac.core
                    :optimizations   :advanced
                    :output-to       "resources/public/js/app.js"
                    :output-dir      "resources/public/js/min"
                    :elide-asserts   true
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}} ]})
