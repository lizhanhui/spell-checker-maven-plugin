source-code-spell-checker
====================
This is a maven plugin, intending to check spelling of source code according to source type. It's designed to be flexible so that minimal changes are required to support new features. Also, this plugin is implemented with performance in mind, whenever possible, we'd reuse existing objects, which should relieve JVM from busy garbage collection as much as possible. 

Another heighlight worth mentioning here is that Damerau Levenshtein Distance is applied when computing similarities between word-pair. This algorithm is very effective as Damerau Levenshtein Distance, compared to traditional Edit Distance(i.e. Leveshtein Distance), treats transposition as one edit, considering the fact more than 80% percent of type errors are transposition of words.

If you want to use this plugin right now in production, I have to warn you this plugin is still at its early development phase. Though it won't bite your hard disk, but keep cautious. 
