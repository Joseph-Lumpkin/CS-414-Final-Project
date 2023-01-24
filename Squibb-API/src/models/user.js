const { DataTypes } = require("sequelize");
const Sequelize = require("../database");

const User = Sequelize.define("User", {
    googleId: {
        type: DataTypes.STRING,
        allowNull: false,
        primaryKey: true
    },
    fName: {
        type: DataTypes.STRING,
        allowNull: true,
    },
    lName: {
        type: DataTypes.STRING,
        allowNull: true,
    },
    email: {
        type: DataTypes.STRING,
        allowNull: true,
    },
    picture: {
        type: DataTypes.STRING,
        allowNull: true,
    },
    createdAt: {
        type: DataTypes.DATE,
    },
    updatedAt: {
        type: DataTypes.DATE,
    }
});

module.exports = User;