# Simple HTTPS server for testing purposes

Generate server.pem with the following command:

```sh
openssl req -new -x509 -keyout key.pem -out server.pem -days 365 -nodes
```

