require('dotenv').config();
const express = require("express");
const morgan = require("morgan");
const helmet = require("helmet");
const cors = require("cors");
const bodyParser = require("body-parser");
const cookieSession = require("cookie-session");

require("./auth/passportGoogle");
require("./models/user");

const api = require("./api");
const passport = require("passport");

const app = express();

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.use(morgan("dev"));
app.use(helmet());
app.use(cors({ origin: `http://localhost:${process.env.PORT}`, credentials: true }));
app.use(express.json());

app.use(
    cookieSession({
        maxAge: 24 * 60 * 60 * 1000,
        keys: [process.env.COOKIE_KEY],
    })
);

app.use(passport.initialize());
app.use(passport.session());

app.get("/", (req, res) => {
    res.json({
        message: "🦄🌈✨👋🌎🌍🌏✨🌈🦄",
    });
});

app.use("/api/v1", api);

module.exports = app;