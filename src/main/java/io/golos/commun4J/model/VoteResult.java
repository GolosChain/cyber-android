package io.golos.commun4J.model;

public class VoteResult {

    public class VoteRequest {
        private CommunName name;
        private CommunName author;
        private String permlink;
        private short weight;

        public VoteRequest(String name, String author, String permlink, short weight) {
            this.name = new CommunName(name);
            this.author = new CommunName(author);
            this.permlink = permlink;
            this.weight = weight;
        }

        public VoteRequest() {
        }

        public void setName(String name) {
            this.name = new CommunName(name);
        }

        public void setAuthor(String author) {
            this.author = new CommunName(author);
        }

        public void setPermlink(String permlink) {
            this.permlink = permlink;
        }

        public void setWeight(short weight) {
            this.weight = weight;
        }

        public String getName() {
            return name.getName();
        }


        public String getAuthor() {
            return author.getName();
        }


        public String getPermlink() {
            return permlink;
        }


        public short getWeight() {
            return weight;
        }

        @Override
        public String toString() {
            return "VoteRequest{" +
                    "name=" + name +
                    ", author=" + author +
                    ", permlink='" + permlink + '\'' +
                    ", weight=" + weight +
                    '}';
        }
    }
}
