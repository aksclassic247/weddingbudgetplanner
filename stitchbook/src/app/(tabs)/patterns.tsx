import { router } from 'expo-router';
import { useState } from 'react';
import { Pressable, ScrollView, StyleSheet, Text, View } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

import { Chip, EmptyState, LevelBadge } from '../../components/ui';
import { LEVELS, type Level, PATTERNS } from '../../data/content';
import { colors, radius } from '../../theme';

export default function PatternsScreen() {
  const [level, setLevel] = useState<Level | 'All'>('All');
  const visible = PATTERNS.filter((p) => level === 'All' || p.level === level);

  return (
    <SafeAreaView style={styles.safe} edges={['top']}>
      <ScrollView contentContainerStyle={styles.content}>
        <Text style={styles.heading}>Get inspired</Text>
        <Text style={styles.sub}>Patterns for every skill level. Start one and track each round.</Text>

        <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={styles.chips}>
          {(['All', ...LEVELS] as const).map((l) => (
            <Chip key={l} label={l} active={level === l} onPress={() => setLevel(l)} />
          ))}
        </ScrollView>

        {visible.length === 0 ? (
          <EmptyState title="No patterns here yet" body="Try another level." />
        ) : (
          <View style={styles.grid}>
            {visible.map((p) => (
              <Pressable
                key={p.id}
                onPress={() => router.push(`/pattern/${p.id}`)}
                accessibilityRole="button"
                style={styles.card}
              >
                <View style={[styles.cover, { backgroundColor: p.color }]}>
                  <Text style={styles.emoji}>{p.emoji}</Text>
                </View>
                <View style={styles.cardBody}>
                  <Text style={styles.cardTitle} numberOfLines={2}>
                    {p.title}
                  </Text>
                  <LevelBadge level={p.level} />
                  <Text style={styles.craft}>{p.craft === 'knitting' ? 'Knitting' : 'Crochet'}</Text>
                </View>
              </Pressable>
            ))}
          </View>
        )}
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1, backgroundColor: colors.cream },
  content: { padding: 16, gap: 14, paddingBottom: 32 },
  heading: { fontSize: 28, fontWeight: '800', color: colors.ink },
  sub: { fontSize: 14, color: colors.inkSoft, lineHeight: 20, marginTop: -6 },
  chips: { gap: 8, paddingVertical: 2 },

  grid: { flexDirection: 'row', flexWrap: 'wrap', justifyContent: 'space-between', rowGap: 12 },
  card: {
    width: '48%',
    backgroundColor: colors.white,
    borderRadius: radius.md,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: colors.line,
  },
  cover: { height: 110, alignItems: 'center', justifyContent: 'center' },
  emoji: { fontSize: 44 },
  cardBody: { padding: 12, gap: 6 },
  cardTitle: { fontSize: 15, fontWeight: '700', color: colors.ink, minHeight: 38 },
  craft: { fontSize: 12, color: colors.inkSoft },
});
