const fs = require("fs");
const https = require("https");

const httpsAgent = new https.Agent({
  ca: fs.readFileSync("./certs/ca.crt"),
  cert: fs.readFileSync("./certs/frontend.crt"),
  key: fs.readFileSync("./certs/frontend.key"),
});

module.exports = {
  devServer: {
    host: "localhost",
    proxy: {
      "^/backend": {
        target: "https://iot-server.germanywestcentral.cloudapp.azure.com",
        agent: httpsAgent,
        changeOrigin: true,
      },
    },
  },
};
