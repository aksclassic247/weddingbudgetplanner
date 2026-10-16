# How to test a StitchBook pattern

A pattern can only be marked "tested" after a person has made the whole project from the written steps, with the yarn and hook or needles it lists, and checked every stitch count.

## What to record

1. Make the project from the written steps only. Do not fix the pattern in your head while you go.
2. Count the stitches at each round or row marked with a count. Note any that do not match.
3. If something is wrong, write down the round, what the pattern says, and what you did.
4. When the project is finished and every count matches (or the pattern has been corrected and re-tested), fill in these two fields in `shared/content.json`:
   - `"testedOn"`: the date you finished the test, for example `"2026-11-02"`
   - `"testedBy"`: your name or a nickname, for example `"Priya"`

The app then shows "Tested on 2026-11-02 by Priya" on that pattern. Until both fields are filled in, it shows "Untested draft".

## Rules

- Do not fill in these fields for a pattern you have only read.
- If you change a pattern's steps, clear both fields and test it again.
- Keep notes on what went wrong in the pattern's history (this file or a note in your git commit message), so the same mistake is not repeated.

## Patterns waiting for testers

| Pattern | Status |
| --- | --- |
| Crochet Flower | Untested draft |
| Garter Stitch Scarf | Untested draft |
| Ribbed Knit Hat | Untested draft |
| Simple Granny Square | Untested draft |

New patterns, such as the octopus, bear and sweater ideas, should be added only after a tester has made them.
