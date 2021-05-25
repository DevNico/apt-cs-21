package de.thro.messaging.application.dependencies.messagequeue;

import java.util.Objects;

public class MessageQueueConfiguration {
    private final String ip;
    private final String port;
    private final String username;
    private final String password;

    public MessageQueueConfiguration(String ip, String port, String username, String password) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "MessageQueueConfiguration{" +
                "ip='" + getIp() + '\'' +
                ", port='" + getPort() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageQueueConfiguration that = (MessageQueueConfiguration) o;
        return Objects.equals(getIp(), that.getIp()) && Objects.equals(getPort(), that.getPort()) && Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIp(), getPort(), getUsername(), getPassword());
    }
}
