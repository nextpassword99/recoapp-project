import { Router } from "express";
import { Op } from "sequelize";
import Waste from "../models/Waste.js";

const router = Router();

router.get("/wastes", async (req, res) => {
  try {
    const since = Number(req.query.since || 0);
    if (Number.isNaN(since))
      return res.status(400).json({ message: "Invalid since" });

    const items = await Waste.findAll({
      where: { modifiedAt: { [Op.gt]: since } },
      order: [["modifiedAt", "ASC"]],
    });

    return res.json({ items });
  } catch (e) {
    console.error(e);
    return res.status(500).json({ message: "Server error" });
  }
});

router.post("/wastes", async (req, res) => {
  try {
    const { items } = req.body || {};
    if (!Array.isArray(items))
      return res.status(400).json({ message: "items array required" });

    const results = [];
    for (const it of items) {
      const {
        id,
        type,
        quantity,
        location,
        date,
        comment,
        deleted = false,
        modifiedAt,
      } = it || {};
      if (!id || !modifiedAt) {
        results.push({
          id,
          status: "skipped",
          reason: "missing id/modifiedAt",
        });
        continue;
      }

      const existing = await Waste.findOne({ where: { id } });
      if (!existing) {
        await Waste.create({
          id,
          type,
          quantity,
          location,
          date,
          comment,
          deleted,
          modifiedAt,
        });
        results.push({ id, status: "created" });
      } else {
        if (Number(modifiedAt) > Number(existing.modifiedAt)) {
          existing.type = type;
          existing.quantity = quantity;
          existing.location = location;
          existing.date = date;
          existing.comment = comment;
          existing.deleted = !!deleted;
          existing.modifiedAt = modifiedAt;
          await existing.save();
          results.push({ id, status: "updated" });
        } else {
          results.push({ id, status: "ignored_older" });
        }
      }
    }

    return res.json({ results });
  } catch (e) {
    console.error(e);
    return res.status(500).json({ message: "Server error" });
  }
});

export default router;
