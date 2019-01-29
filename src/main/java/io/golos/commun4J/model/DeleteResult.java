package io.golos.commun4J.model;

public class DeleteResult {

    public class DeleteMessageRequest {
        private CommunName author;
        private String permlink;

        public DeleteMessageRequest(String author, String permlink) {
            this.author = new CommunName(author);
            this.permlink = permlink;
        }


        public String getAuthor() {
            return author.getName();
        }


        public String getPermlink() {
            return permlink;
        }

        public DeleteMessageRequest() {
        }

        public void setAuthor(String author) {
            this.author = new CommunName(author);
        }

        public void setPermlink(String permlink) {
            this.permlink = permlink;
        }
    }
}
