import { DataTypes, Model } from "sequelize";
import { sequelize } from "../lib/database.js";
import User from "./User.js";

class Waste extends Model {}

Waste.init(
  {
    id: {
      type: DataTypes.STRING,
      primaryKey: true,
    },
    userId: {
      type: DataTypes.INTEGER,
      allowNull: false,
    },
    type: {
      type: DataTypes.STRING,
      allowNull: false,
    },
    quantity: {
      type: DataTypes.FLOAT,
      allowNull: false,
    },
    location: {
      type: DataTypes.STRING,
      allowNull: false,
    },
    date: {
      type: DataTypes.BIGINT,
      allowNull: false,
    },
    comment: {
      type: DataTypes.TEXT,
      allowNull: true,
    },
    deleted: {
      type: DataTypes.BOOLEAN,
      allowNull: false,
      defaultValue: false,
    },
    modifiedAt: {
      type: DataTypes.BIGINT,
      allowNull: false,
    },
  },
  {
    sequelize,
    modelName: "Waste",
    tableName: "wastes",
    timestamps: true,
    indexes: [{ fields: ["userId"] }, { fields: ["modifiedAt"] }],
  }
);

User.hasMany(Waste, { foreignKey: "userId" });
Waste.belongsTo(User, { foreignKey: "userId" });

export default Waste;
