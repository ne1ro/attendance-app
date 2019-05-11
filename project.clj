(defproject attendance-app "0.1.0-SNAPSHOT"
  :description "Attendance Mobile Application"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.339"]
                 [com.bhauman/rebel-readline "0.1.4"]
                 [com.andrewmcveigh/cljs-time "0.5.2"]
                 [com.7theta/re-frame-fx "0.2.1"]
                 [reagent "0.8.1" :exclusions [cljsjs/react cljsjs/react-dom cljsjs/react-dom-server cljsjs/create-react-class]]
                 [re-frame "0.10.6"]]
  :repositories [["jitpack" "https://jitpack.io"]]
  :plugins [[lein-cljsbuild "1.1.4"]
            [lein-cljfmt "0.6.3"]
            [lein-kibit "0.1.6"]
            [lein-figwheel "0.5.18"]]
  :clean-targets ["target/" "index.ios.js" "index.android.js" #_($PLATFORM_CLEAN$)]
  :aliases {"prod-build"     ^{:doc "Recompile code with prod profile."}
                             ["do" "clean"
                              ["with-profile" "prod" "cljsbuild" "once"]]
            "advanced-build" ^{:doc "Recompile code for production using :advanced compilation."}
                             ["do" "clean"
                              ["with-profile" "advanced" "cljsbuild" "once"]]}
  :jvm-opts ["-XX:+IgnoreUnrecognizedVMOptions" "--add-modules jdk.scripting.nashorn"]
  :profiles {:dev      {:dependencies [[figwheel-sidecar "0.5.18"]
                                       [cljfmt "0.5.1"]
                                       [cider/piggieback "0.3.10"] [http-kit "2.3.0"]]
                        :plugins      [[jonase/eastwood "0.3.4" :exclusions [org.clojure/clojure]]]
                        :source-paths ["src" "env/dev"]
                        :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}
                        :cljsbuild    {:builds [{:id           "ios"
                                                 :source-paths ["src" "env/dev"]
                                                 :figwheel     {:websocket-host "localhost"}
                                                 :compiler     {:output-to     "target/ios/index.js"
                                                                :main          "env.ios.main"
                                                                :output-dir    "target/ios"
                                                                :optimizations :none
                                                                :cache-analysis true
                                                                :target        :nodejs}}
                                                {:id           "android"
                                                 :source-paths ["src" "env/dev"]
                                                 :figwheel     {:websocket-host "localhost"}
                                                 :compiler     {:output-to     "target/android/index.js"
                                                                :main          "env.android.main"
                                                                :output-dir    "target/android"
                                                                :optimizations :none
                                                                :cache-analysis true
                                                                :target        :nodejs}}
                                                #_($DEV_PROFILES$)]}}
             :prod     {:cljsbuild {:builds [{:id           "ios"
                                              :source-paths ["src" "env/prod"]
                                              :compiler     {:output-to          "index.ios.js"
                                                             :main               "env.ios.main"
                                                             :output-dir         "target/ios"
                                                             :static-fns         true
                                                             :optimize-constants true
                                                             :optimizations      :simple
                                                             :target             :nodejs
                                                             :closure-defines    {"goog.DEBUG" false}}}
                                             {:id           "android"
                                              :source-paths ["src" "env/prod"]
                                              :compiler     {:output-to          "index.android.js"
                                                             :main               "env.android.main"
                                                             :output-dir         "target/android"
                                                             :static-fns         true
                                                             :optimize-constants true
                                                             :fn-invoke-direct true
                                                             :warnings true
                                                             :pseudo-names true
                                                             :print-input-delimiter true
                                                             :rewrite-polyfills true
                                                             :infer-externs true
                                                             :optimizations      :simple
                                                             :verbose true
                                                             :target             :nodejs
                                                             :language-in :es-next
                                                             :language-out :no-transpile
                                                             :parallel-build true
                                                             :closure-defines    {"goog.DEBUG" false}}}
                                             #_($PROD_PROFILES$)]}}
             :advanced {:dependencies [[react-native-externs "0.2.0"]]
                        :cljsbuild    {:builds [{:id           "ios"
                                                 :source-paths ["src" "env/prod"]
                                                 :compiler     {:output-to          "index.ios.js"
                                                                :main               "env.ios.main"
                                                                :output-dir         "target/ios"
                                                                :static-fns         true
                                                                :optimize-constants true
                                                                :optimizations      :advanced
                                                                :target             :nodejs
                                                                :closure-defines    {"goog.DEBUG" false}}}
                                                {:id           "android"
                                                 :source-paths ["src" "env/prod"]
                                                 :compiler     {:output-to          "index.android.js"
                                                                :main               "env.android.main"
                                                                :output-dir         "target/android"
                                                                :static-fns         true
                                                                :optimize-constants true
                                                                :optimizations      :advanced
                                                                :target             :nodejs
                                                               :fn-invoke-direct true
                                                               :warnings true
                                                               :pseudo-names true
                                                               :print-input-delimiter true
                                                               :rewrite-polyfills true
                                                               :infer-externs true
                                                               :verbose true
                                                               :language-in :es-next
                                                               :language-out :no-transpile
                                                               :parallel-build true
                                                                :closure-defines    {"goog.DEBUG" false}}}
                                                #_($ADVANCED_PROFILES$)]}}})
