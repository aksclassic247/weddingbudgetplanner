import { Ionicons } from '@expo/vector-icons';
import { router } from 'expo-router';
import { useState } from 'react';
import { Pressable, ScrollView, StyleSheet, Text, View } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

import { LevelBadge, Segmented } from '../../components/ui';
import { type Craft, STITCHES, TUTORIALS } from '../../data/content';
import { colors, radius } from '../../theme';

const CRAFT_OPTIONS: { label: string; value: Craft }[] = [
  { label: 'Knitting', value: 'knitting' },
  { label: 'Crochet', value: 'crochet' },
];

export default function LearnScreen() {
  const [craft, setCraft] = useState<Craft>('knitting');
  const tutorials = TUTORIALS.filter((t) => t.craft === craft);
  const stitches = STITCHES.filter((s) => s.craft === craft);

  return (
    <SafeAreaView style={styles.safe} edges={['top']}>
      <ScrollView contentContainerStyle={styles.content}>
        <Text style={styles.heading}>Learn</Text>
        <Segmented options={CRAFT_OPTIONS} value={craft} onChange={setCraft} />

        <Text style={styles.sectionTitle}>Tutorials</Text>
        {tutorials.map((t) => (
          <Pressable
            key={t.id}
            onPress={() => router.push(`/tutorial/${t.id}`)}
            accessibilityRole="button"
            style={styles.card}
          >
            <View style={[styles.swatch, { backgroundColor: t.color }]} />
            <View style={styles.cardBody}>
              <Text style={styles.cardTitle}>{t.title}</Text>
              <Text style={styles.cardSub} numberOfLines={2}>
                {t.summary}
              </Text>
              <View style={styles.cardMeta}>
                <LevelBadge level={t.level} />
                <Text style={styles.minutes}>{t.minutes} min</Text>
              </View>
            </View>
            <Ionicons name="chevron-forward" size={18} color={colors.inkSoft} />
          </Pressable>
        ))}

        <Text style={styles.sectionTitle}>Abbreviations</Text>
        <View style={styles.glossary}>
          {stitches.map((s, i) => (
            <View key={s.abbr} style={[styles.glossRow, i > 0 && styles.glossBorder]}>
              <Text style={styles.abbr}>{s.abbr}</Text>
              <View style={{ flex: 1 }}>
                <Text style={styles.glossName}>{s.name}</Text>
                <Text style={styles.glossMeaning}>{s.meaning}</Text>
              </View>
            </View>
          ))}
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1, backgroundColor: colors.cream },
  content: { padding: 16, gap: 14, paddingBottom: 32 },
  heading: { fontSize: 28, fontWeight: '800', color: colors.ink },
  sectionTitle: { fontSize: 18, fontWeight: '700', color: colors.ink, marginTop: 6 },

  card: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
    backgroundColor: colors.white,
    borderRadius: radius.md,
    padding: 12,
    borderWidth: 1,
    borderColor: colors.line,
  },
  swatch: { width: 64, height: 64, borderRadius: radius.sm },
  cardBody: { flex: 1, gap: 4 },
  cardTitle: { fontSize: 16, fontWeight: '700', color: colors.ink },
  cardSub: { fontSize: 13, color: colors.inkSoft, lineHeight: 18 },
  cardMeta: { flexDirection: 'row', alignItems: 'center', gap: 10, marginTop: 4 },
  minutes: { fontSize: 12, color: colors.inkSoft },

  glossary: {
    backgroundColor: colors.white,
    borderRadius: radius.md,
    borderWidth: 1,
    borderColor: colors.line,
    overflow: 'hidden',
  },
  glossRow: { flexDirection: 'row', gap: 12, padding: 14, alignItems: 'flex-start' },
  glossBorder: { borderTopWidth: 1, borderTopColor: colors.line },
  abbr: {
    minWidth: 56,
    fontSize: 15,
    fontWeight: '700',
    color: colors.coralDark,
    fontVariant: ['tabular-nums'],
  },
  glossName: { fontSize: 15, fontWeight: '600', color: colors.ink },
  glossMeaning: { fontSize: 13, color: colors.inkSoft, marginTop: 2, lineHeight: 18 },
});
