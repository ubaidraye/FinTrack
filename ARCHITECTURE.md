# FinTrack Architecture Decisions

## Decision 001 - Feature-first package structure

### Decision
The project uses a feature-first package structure instead of a layer-based structure.

### Reason
- Easier to scale.
- Keeps related code together.
- Improves maintainabilty as the project grows.

---

## Decision 002 - Transaction model

### Decision
Income and expenses are stored in a single "Transaction" entity.

### Reason
- Both represents financial transactions.
- Reduces duplicate code.
- Simplifies querying, searching, and reporting.

---

### Decision 003 - Category model

### Decision
Categories are stored in a separate ROOM table.

### Reason
- Allows user defined categories.
- Support custom icons.
- Makes the system more extensible.

---

### Decision 004 - Default category

### Decision
Every transaction belongs to a category.

If the user doesn't choose one, it is assigned to **Other**.

### Reason
- Avoids nullable category references.
- Simplifies reports and charts.
- Ensures consistent state.

---

### Decision 005 - Delete behaviour
Deleting a transaction depends on how the action is performed.

- Delete button -> Confirmation dialog.
- Swipe to delete -> Undo Snackbar.

### Reason
- Prevents accidental data loss.
- Follows common Material Design interaction pattern.
