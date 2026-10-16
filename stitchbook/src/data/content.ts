export type Level = 'Easy' | 'Beginner' | 'Intermediate' | 'Advanced';
export type Craft = 'knitting' | 'crochet';

export type TutorialStep = {
  title: string;
  text: string;
};

export type Tutorial = {
  id: string;
  craft: Craft;
  title: string;
  level: Level;
  minutes: number;
  color: string;
  summary: string;
  steps: TutorialStep[];
};

export type Pattern = {
  id: string;
  craft: Craft;
  title: string;
  level: Level;
  color: string;
  emoji: string;
  time: string;
  materials: string[];
  gauge?: string;
  // Each entry is one round or section, written the way it would be in a pattern.
  steps: string[];
};

export const LEVELS: Level[] = ['Easy', 'Beginner', 'Intermediate', 'Advanced'];

export const LEVEL_COLORS: Record<Level, string> = {
  Easy: '#7BC47F',
  Beginner: '#F2A65A',
  Intermediate: '#E76F5E',
  Advanced: '#8E6CC9',
};

export const TUTORIALS: Tutorial[] = [
  {
    id: 'long-tail-cast-on',
    craft: 'knitting',
    title: 'Long-Tail Cast On',
    level: 'Beginner',
    minutes: 5,
    color: '#8FB8DE',
    summary: 'The most common way to start a knitting project.',
    steps: [
      {
        title: 'Leave a tail',
        text: 'Pull out about one inch of yarn for every stitch you need, plus a few extra inches. Make a slip knot and place it on your needle.',
      },
      {
        title: 'Hold the yarn',
        text: 'Wrap the tail end around your left thumb and the working yarn around your left index finger. Hold both strands with your other fingers.',
      },
      {
        title: 'Make the first stitch',
        text: 'Bring the needle up through the loop on your thumb, catch the strand from your index finger, and pull it back through the thumb loop. Drop the thumb loop and tighten gently.',
      },
      {
        title: 'Repeat',
        text: 'Keep going until you have the number of stitches your pattern asks for. Keep the tension even, not too tight.',
      },
    ],
  },
  {
    id: 'knit-stitch',
    craft: 'knitting',
    title: 'The Knit Stitch',
    level: 'Beginner',
    minutes: 6,
    color: '#E8C36A',
    summary: 'Learn the basic knit (k) stitch, the foundation of garter stitch.',
    steps: [
      {
        title: 'Set up',
        text: 'Hold the needle with the cast-on stitches in your left hand. Keep the working yarn behind the work.',
      },
      {
        title: 'Insert',
        text: 'Slide the right needle into the front of the first stitch on the left needle, from left to right.',
      },
      {
        title: 'Wrap',
        text: 'Wrap the yarn counterclockwise around the tip of the right needle.',
      },
      {
        title: 'Pull through',
        text: 'Pull the loop through the stitch with the right needle and let the old stitch slip off the left needle.',
      },
    ],
  },
  {
    id: 'purl-stitch',
    craft: 'knitting',
    title: 'The Purl Stitch',
    level: 'Beginner',
    minutes: 6,
    color: '#A5C9A0',
    summary: 'The purl (p) stitch is the reverse of knit and creates ribbing.',
    steps: [
      {
        title: 'Bring yarn forward',
        text: 'Keep the yarn in front of the work.',
      },
      {
        title: 'Insert',
        text: 'Slide the right needle into the front of the next stitch from right to left.',
      },
      {
        title: 'Wrap',
        text: 'Wrap the yarn counterclockwise around the right needle, going over the top and then under.',
      },
      {
        title: 'Pull through',
        text: 'Draw the loop back through the stitch and slip the old stitch off the left needle.',
      },
    ],
  },
  {
    id: 'bind-off',
    craft: 'knitting',
    title: 'Basic Bind Off',
    level: 'Beginner',
    minutes: 5,
    color: '#D9A3B8',
    summary: 'Finish your knitting so the edge stays neat and does not unravel.',
    steps: [
      {
        title: 'Knit two',
        text: 'Knit the first two stitches as normal.',
      },
      {
        title: 'Pass over',
        text: 'Insert the left needle into the first stitch you knitted and lift it over the second and off the needle.',
      },
      {
        title: 'Knit one more',
        text: 'Knit the next stitch so there is one stitch on the right needle again, then pass the previous one over it.',
      },
      {
        title: 'Finish',
        text: 'Repeat until one stitch is left. Cut the yarn, leave a tail, and pull it through the last loop.',
      },
    ],
  },
  {
    id: 'magic-ring',
    craft: 'crochet',
    title: 'Magic Ring',
    level: 'Easy',
    minutes: 5,
    color: '#E76F5E',
    summary: 'Start amigurumi and flower projects with a tight, closed center.',
    steps: [
      {
        title: 'Wrap',
        text: 'Wrap the yarn around two fingers, leaving a tail of about six inches.',
      },
      {
        title: 'Insert',
        text: 'Insert the hook into the ring, catch the working yarn, and pull up a loop.',
      },
      {
        title: 'Chain',
        text: 'Chain the number of stitches your pattern asks for (usually one or two).',
      },
      {
        title: 'Close the ring',
        text: 'Work your stitches into the ring, then pull the tail to close it tightly.',
      },
    ],
  },
  {
    id: 'single-crochet',
    craft: 'crochet',
    title: 'Single Crochet (sc)',
    level: 'Beginner',
    minutes: 7,
    color: '#F2A65A',
    summary: 'The shortest and densest basic stitch, great for amigurumi.',
    steps: [
      {
        title: 'Insert',
        text: 'Insert the hook into the next stitch, under both loops of the top.',
      },
      {
        title: 'Yarn over',
        text: 'Yarn over and pull up a loop. You now have two loops on the hook.',
      },
      {
        title: 'Yarn over again',
        text: 'Yarn over one more time and pull through both loops.',
      },
      {
        title: 'Repeat',
        text: 'You have one single crochet. Move to the next stitch and repeat.',
      },
    ],
  },
  {
    id: 'double-crochet',
    craft: 'crochet',
    title: 'Double Crochet (dc)',
    level: 'Beginner',
    minutes: 7,
    color: '#7BC47F',
    summary: 'Taller and more open than sc, so projects grow faster.',
    steps: [
      {
        title: 'Yarn over',
        text: 'Yarn over before you insert the hook into the stitch.',
      },
      {
        title: 'Insert and pull up',
        text: 'Insert the hook into the stitch and pull up a loop. You have three loops on the hook.',
      },
      {
        title: 'Two at a time',
        text: 'Yarn over and pull through two loops, then yarn over and pull through the last two.',
      },
      {
        title: 'Repeat',
        text: 'Move to the next stitch and repeat the same steps.',
      },
    ],
  },
  {
    id: 'granny-square',
    craft: 'crochet',
    title: 'Making a Granny Square',
    level: 'Intermediate',
    minutes: 12,
    color: '#8E6CC9',
    summary: 'Build a square with corners using clusters and chain spaces.',
    steps: [
      {
        title: 'Start the center',
        text: 'Make a magic ring, ch 3, then work 2 dc into the ring. Ch 2, and work (3 dc, ch 2) three times into the ring.',
      },
      {
        title: 'Join the round',
        text: 'Slip stitch to the top of the first ch 3 to close the round.',
      },
      {
        title: 'Work the corners',
        text: 'Slip stitch into the next chain space. Ch 3, then (2 dc, ch 2, 3 dc) in the same space to make a corner.',
      },
      {
        title: 'Continue around',
        text: 'Ch 1, then (3 dc, ch 2, 3 dc) in each corner space. Slip stitch to finish.',
      },
    ],
  },
];

export type Stitch = { abbr: string; name: string; craft: Craft; meaning: string };

export const STITCHES: Stitch[] = [
  { abbr: 'k', name: 'Knit', craft: 'knitting', meaning: 'Insert the needle front to back, wrap, and pull through.' },
  { abbr: 'p', name: 'Purl', craft: 'knitting', meaning: 'Insert the needle back to front, wrap, and pull through.' },
  { abbr: 'k2tog', name: 'Knit two together', craft: 'knitting', meaning: 'Knit two stitches as one, which decreases by one.' },
  { abbr: 'st st', name: 'Stockinette stitch', craft: 'knitting', meaning: 'Knit one row, purl the next. Smooth on one side.' },
  { abbr: 'ch', name: 'Chain', craft: 'crochet', meaning: 'The base stitch. Yarn over and pull through the loop.' },
  { abbr: 'sl st', name: 'Slip stitch', craft: 'crochet', meaning: 'The shortest stitch. Used for joining rounds and moving.' },
  { abbr: 'sc', name: 'Single crochet', craft: 'crochet', meaning: 'Insert, yarn over, pull up, yarn over, pull through both loops.' },
  { abbr: 'dc', name: 'Double crochet', craft: 'crochet', meaning: 'Yarn over first, then pull up and work two loops off, twice.' },
];

export const PATTERNS: Pattern[] = [
  {
    id: 'crochet-flower',
    craft: 'crochet',
    title: 'Crochet Flower',
    level: 'Easy',
    color: '#F4B6C2',
    emoji: '🌸',
    time: 'About 30 min',
    materials: ['Worsted or DK cotton in two colors', '4.0 mm (G-6) hook', 'Tapestry needle and scissors'],
    steps: [
      'Round 1: Make a magic ring. Ch 2, work 12 dc into the ring, sl st to the top of the first ch 2 to join. (12 sts)',
      'Round 2: *Sl st into the next dc, ch 4, skip 1 dc.* Repeat from * 5 more times to make 6 loops. Sl st to join.',
      'Round 3: In each ch-4 loop work (sc, hdc, 2 dc, hdc, sc) to make six petals. Sl st into the base to finish.',
      'Finish: Cut the yarn, pull it through the last loop, and weave in the ends.',
    ],
  },
  {
    id: 'garter-scarf',
    craft: 'knitting',
    title: 'Garter Stitch Scarf',
    level: 'Beginner',
    color: '#E8C36A',
    emoji: '🧣',
    time: 'About a weekend',
    materials: ['Worsted weight yarn, about 300 yards', 'US 8 (5.0 mm) needles', 'Tapestry needle'],
    gauge: 'About 18 sts and 36 rows = 4 in in garter stitch',
    steps: [
      'Cast on 30 stitches using the long-tail cast on.',
      'Knit every row until the scarf measures about 60 inches, or until your yarn is almost gone. Leave enough yarn for the bind off.',
      'Bind off loosely using the basic bind off.',
      'Weave in all ends and block lightly.',
    ],
  },
  {
    id: 'ribbed-beanie',
    craft: 'knitting',
    title: 'Ribbed Knit Hat',
    level: 'Beginner',
    color: '#8B6A4F',
    emoji: '🧶',
    time: 'About 2 days',
    materials: ['Worsted weight wool, about 200 yards', 'US 7 (4.5 mm) 16 in circular needle for ribbing', 'US 8 (5.0 mm) 16 in circular needle for the body', 'Stitch marker'],
    gauge: 'About 20 sts = 4 in in stockinette stitch on US 8',
    steps: [
      'Cast on 80 stitches on US 7 needle, join in the round, and place a marker.',
      'Work k2, p2 ribbing for 2.5 inches (about 20 rounds).',
      'Switch to US 8 needle. Knit every round until the hat measures 6 inches from the cast-on edge.',
      'Crown: *K8, k2tog* around (72 sts). Knit 1 round.',
      'Repeat the decrease round, knitting one fewer stitch before each k2tog each time, until 8 sts remain.',
      'Cut the yarn, thread it through the last 8 stitches, and pull tight. Weave in the ends.',
    ],
  },
  {
    id: 'granny-square-pouch',
    craft: 'crochet',
    title: 'Simple Granny Square',
    level: 'Intermediate',
    color: '#B9D7EA',
    emoji: '🟦',
    time: 'About 1 hour',
    materials: ['Worsted cotton in four colors', '4.5 mm (7) hook', 'Tapestry needle'],
    gauge: 'About 4 in square',
    steps: [
      'Round 1: Magic ring, ch 3 (counts as dc), 2 dc, ch 2, (3 dc, ch 2) three times. Sl st to the top of the first ch 3.',
      'Round 2: Sl st into the next ch-2 space. Ch 3, then (2 dc, ch 2, 3 dc) in the same space to make the first corner. *Ch 1, (3 dc, ch 2, 3 dc) in the next corner space.* Repeat from * two more times. Ch 1, sl st to the top of the first ch 3 to join. (4 corners)',
      'Finish: Fasten off, weave in the ends, and join squares as needed.',
    ],
  },
];

export function getTutorial(id: string) {
  return TUTORIALS.find((t) => t.id === id);
}

export function getPattern(id: string) {
  return PATTERNS.find((p) => p.id === id);
}
