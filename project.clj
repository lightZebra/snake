(defproject snake "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]

                 ; console
                 [org.jline/jline "3.21.0"]
                 [org.fusesource.jansi/jansi "2.4.0"]]
  :repl-options {:init-ns snake.core}
  :main snake.core)
