package my.jwds.core.api;

public enum InvokeContentType {
    url(0),
    param(1),
    json(2),
    file(3);

    InvokeContentType(int order) {
        this.order = order;
    }

    int order;

    public int getOrder() {
        return order;
    }
}
