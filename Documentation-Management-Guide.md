# Documentation Management System for Claude Code

*A comprehensive system to ensure CLAUDE.md and project documentation are always created, maintained, and kept up-to-date.*

---

## Overview

This documentation management system provides automated enforcement of documentation standards through:

1. **docs-manager Subagent**: Proactively manages documentation
2. **Documentation Rules**: Enforces standards on all files
3. **docs Skill**: Manual documentation updates via `/docs`
4. **Hooks**: Automated verification at key points

---

## Quick Setup

### 1. Copy Configuration Files

Copy these to your project's `.claude/` directory:

```
.claude/
├── agents/
│   └── docs-manager.md          # Documentation manager subagent
├── skills/
│   └── docs/
│       └── SKILL.md             # /docs skill for manual updates
├── rules/
│   └── documentation.md         # Documentation enforcement rules
└── settings.json                # Hooks configuration
```

### 2. Copy Hook Scripts

```
scripts/
└── hooks/
    ├── check-docs.sh            # Verifies CLAUDE.md exists and is complete
    └── post-feature-docs.sh     # Reminds to update docs after changes
```

Make them executable:
```bash
chmod +x scripts/hooks/*.sh
```

### 3. Verify Installation

Run `/agents` in Claude Code to see `docs-manager` listed.

---

## How It Works

### Automatic Enforcement

The system enforces documentation at multiple points:

| Trigger | Action |
|---------|--------|
| **Session Start** | Verify CLAUDE.md exists |
| **After Code Changes** | Remind to update docs if many files changed |
| **Session End** | Final documentation check |
| **Proactive** | docs-manager auto-invoked after features |

### docs-manager Subagent

The `docs-manager` subagent is marked to "use proactively" which means Claude will automatically delegate documentation tasks to it when:

- Implementing new features
- Significant refactoring completed
- Architectural changes made
- New dependencies added
- Build configuration modified
- New modules created

### /docs Skill

Invoke manually with:
```
/docs              # Update all documentation
/docs claude.md    # Update only CLAUDE.md
/docs readme       # Update only README.md
```

### Documentation Rules

The `documentation.md` rules file applies to all files (`**/*`) and enforces:

- CLAUDE.md must exist
- Must have required sections
- Must be updated after significant changes
- Inline code documentation standards

---

## CLAUDE.md Requirements

### Mandatory Sections

Every CLAUDE.md MUST include:

```markdown
## Tech Stack
[Languages, frameworks, versions]

## Commands
[Build, test, lint - must be copy-paste ready]

## Verification
[How to verify changes work]

## Critical Rules
[IMPORTANT items to follow]
```

### Quality Standards

| Requirement | Standard |
|-------------|----------|
| **Length** | Under 100 lines (ideal: 50-80) |
| **Commands** | Must work when copy-pasted |
| **Content** | Only what Claude can't infer |
| **Rules** | Specific and actionable |
| **Updates** | After every significant change |

### What NOT to Include

- Generic advice ("write clean code")
- Standard language conventions
- Information that changes frequently
- Detailed tutorials (link instead)
- File-by-file descriptions

---

## Hook Scripts

### check-docs.sh

Runs at session start and end:

```bash
#!/bin/bash
# Verifies:
# 1. CLAUDE.md exists (root or .claude/)
# 2. Has required sections
# 3. Warns if too long
# 4. Reminds if code changed without docs update
```

**Exit Codes:**
- `0`: Docs check passed
- `2`: Blocks action (CLAUDE.md missing - critical)

### post-feature-docs.sh

Runs after Write/Edit operations:

```bash
#!/bin/bash
# Tracks code file changes
# After 10+ changes, reminds to run /docs
```

---

## Enforcement Workflow

### New Project

1. **Session starts** → `check-docs.sh` runs
2. **CLAUDE.md missing** → Blocks with error message
3. **Claude creates CLAUDE.md** → Uses template
4. **User verifies** → Session continues

### During Development

1. **Feature implemented** → docs-manager invoked proactively
2. **docs-manager audits** → Checks if docs need update
3. **Updates applied** → CLAUDE.md stays current

### Before Commit

1. **Session ending** → `check-docs.sh` runs
2. **Docs outdated** → Warning displayed
3. **Claude suggests** → "Consider running /docs"

---

## Customization

### Adjust Strictness

In `check-docs.sh`, change exit codes:

```bash
# Strict mode: block on missing docs
exit 2

# Warning mode: warn but don't block
exit 0
```

### Add More Sections

In `documentation.md`, add required sections:

```markdown
### Mandatory Sections
- [ ] Tech Stack
- [ ] Commands
- [ ] Verification
- [ ] Critical Rules
- [ ] YOUR_NEW_SECTION  # Add here
```

### Integrate with CI/CD

Add to your CI pipeline:

```yaml
# GitHub Actions example
- name: Check Documentation
  run: ./scripts/hooks/check-docs.sh
```

---

## Subagent Integration

### Proactive Invocation

The docs-manager description includes "use proactively" which tells Claude to automatically delegate when documentation work is needed.

### Manual Invocation

You can also request it directly:

```
Use the docs-manager to audit our documentation
Ask docs-manager to update CLAUDE.md after these changes
```

### Orchestration Pattern

For large projects, use orchestrator pattern:

```
Orchestrator
    ↓
├── code-reviewer (reviews code)
├── test-runner (runs tests)
└── docs-manager (updates docs) ← Called after features
```

---

## Best Practices

### 1. Update Incrementally

Don't wait until project end. Update after each feature:

```
Implement feature → docs-manager updates → Commit
```

### 2. Review Doc Changes

Treat documentation changes like code:

```bash
git diff CLAUDE.md  # Review changes
git add CLAUDE.md
git commit -m "docs: update for new feature"
```

### 3. Keep It Concise

More docs ≠ better docs. Follow the rule:

> "Would removing this line cause Claude to make mistakes?"
>
> If no → Delete it

### 4. Test Commands

Every command in CLAUDE.md should be tested:

```bash
# Before committing, verify:
npm run dev      # Works?
npm run test     # Works?
npm run build    # Works?
```

### 5. Use Imports for Details

Keep CLAUDE.md short by importing:

```markdown
## Architecture
See @docs/architecture.md for details

## API Design
See @docs/api-conventions.md
```

---

## Troubleshooting

### "CLAUDE.md not found"

Create one at project root:
```bash
touch CLAUDE.md
```
Or use `/init` to generate automatically.

### "Missing required sections"

Add the missing sections. Minimum template:

```markdown
## Tech Stack
- [Your stack here]

## Commands
- `command`: description

## Verification
- Run `test command` to verify

## Critical Rules
IMPORTANT: [Your rules here]
```

### "CLAUDE.md too long"

- Move detailed docs to separate files
- Use @imports to reference them
- Delete generic/obvious content
- Focus on what Claude needs to know

### "Docs not updating"

Check that:
1. docs-manager is in `.claude/agents/`
2. documentation.md is in `.claude/rules/`
3. Hooks are configured in settings.json
4. Scripts are executable

---

## File Reference

### Created Files

```
.claude/
├── agents/
│   └── docs-manager.md          # Documentation subagent
├── skills/
│   └── docs/
│       └── SKILL.md             # /docs command
├── rules/
│   └── documentation.md         # Enforcement rules
└── settings.json                # Hooks config

scripts/
└── hooks/
    ├── check-docs.sh            # Existence check
    └── post-feature-docs.sh     # Change reminder
```

### Integration Points

| Component | Purpose | Trigger |
|-----------|---------|---------|
| docs-manager | Proactive doc management | After features |
| /docs skill | Manual doc updates | User invokes `/docs` |
| documentation.md | Rules enforcement | All file operations |
| check-docs.sh | Existence verification | Session start/end |
| post-feature-docs.sh | Update reminders | After code edits |

---

*This system ensures your documentation stays current with minimal manual effort.*
