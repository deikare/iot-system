const fs = require("fs");
const https = require("https");

const httpsAgent = new https.Agent({
  ca: fs.readFileSync("./certs/ca.crt"),
  cert: fs.readFileSync("./certs/frontend.crt"),
  key: fs.readFileSync("./certs/frontend.key"),
});

const serverFQDN = process.env.SERVER_FQDN;

module.exports = {
  devServer: {
    host: "localhost",
    proxy: {
      "^/backend": {
        target: `https://${serverFQDN}`,
        agent: httpsAgent,
        changeOrigin: true,
      },
    },
  },
};
