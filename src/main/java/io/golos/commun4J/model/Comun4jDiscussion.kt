package io.golos.commun4J.model

class Comun4jDiscussion(var author: String,
                        var permlink: String,
                        var body: String,
                        var title: String){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Comun4jDiscussion

        if (author != other.author) return false
        if (permlink != other.permlink) return false
        if (body != other.body) return false
        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        var result = author.hashCode()
        result = 31 * result + permlink.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + title.hashCode()
        return result
    }

    override fun toString(): String {
        return "Comun4jDiscussion(author='$author', permlink='$permlink', body='$body', title='$title')"
    }

}

