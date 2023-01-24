const express = require('express');
const passport = require('passport');
const router = express.Router();

const errorLoginUrl = "http://localhost:3000/login/error";
const successLoginUrl = "http://localhost:3000/login/success";

router.get(
    "/login/google",
    passport.authenticate("google", { scope: ["profile", "email"] })
);

router.get(
    "/auth/google/callback",
    passport.authenticate("google", {
        failureMessage: "Unable to login with Google, please try again later.",
        failureRedirect: errorLoginUrl,
        successRedirect: successLoginUrl,
    }),
    (req, res) => {
        console.log("User: ", req.user);
        res.send("Thank you for signing in with Google!");
    }
);

module.exports = router;