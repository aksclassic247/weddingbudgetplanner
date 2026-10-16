import { Ionicons } from '@expo/vector-icons';
import { router } from 'expo-router';
import type { ComponentProps } from 'react';
import { ScrollView, StyleSheet, Text, View, Pressable } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

import { ProgressBar, PrimaryButton } from '../../components/ui';
import { useProjects } from '../../context/ProjectsContext';
import { getPattern, TUTORIALS } from '../../data/content';
import { colors, radius } from '../../theme';

export default function HomeScreen() {
  const { projects } = useProjects();
  const latest = projects[0];
  const latestPattern = latest ? getPattern(latest.patternId) : undefined;
  const latestDone = latest ? latest.done.filter(Boolean).length : 0;

  return (
    <SafeAreaView style={styles.safe} edges={['top']}>
      <ScrollView contentContainerStyle={styles.content}>
        <View style={styles.hero}>
          <Text style={styles.heroTitle}>Easy to follow crochet & knit</Text>
          <Text style={styles.heroSub}>Step by step tutorials, patterns, and project tracking in one place.</Text>
          <PrimaryButton label="Start learning" variant="light" onPress={() => router.push('/learn')} style={styles.heroButton} />
        </View>

        {latest && latestPattern ? (
          <Pressable
            onPress={() => router.push(`/project/${latest.id}`)}
            accessibilityRole="button"
            style={styles.card}
          >
            <Text style={styles.sectionLabel}>Continue your project</Text>
            <Text style={styles.cardTitle}>
              {latestPattern.emoji} {latestPattern.title}
            </Text>
            <ProgressBar value={latestDone} total={latest.done.length} />
          </Pressable>
        ) : null}

        <Text style={styles.sectionTitle}>Quick start</Text>
        <View style={styles.actions}>
          <ActionTile icon="book-outline" label="Learn stitches" onPress={() => router.push('/learn')} />
          <ActionTile icon="grid-outline" label="Browse patterns" onPress={() => router.push('/patterns')} />
          <ActionTile icon="bookmark-outline" label="My projects" onPress={() => router.push('/projects')} />
        </View>

        <Text style={styles.sectionTitle}>Beginner friendly tutorials</Text>
        {TUTORIALS.filter((t) => t.level === 'Easy' || t.level === 'Beginner')
          .slice(0, 4)
          .map((t) => (
            <Pressable
              key={t.id}
              onPress={() => router.push(`/tutorial/${t.id}`)}
              accessibilityRole="button"
              style={styles.row}
            >
              <View style={[styles.swatch, { backgroundColor: t.color }]} />
              <View style={{ flex: 1 }}>
                <Text style={styles.rowTitle}>{t.title}</Text>
                <Text style={styles.rowSub}>
                  {t.minutes} min · {t.craft === 'knitting' ? 'Knitting' : 'Crochet'}
                </Text>
              </View>
              <Ionicons name="chevron-forward" size={18} color={colors.inkSoft} />
            </Pressable>
          ))}
      </ScrollView>
    </SafeAreaView>
  );
}

function ActionTile({
  icon,
  label,
  onPress,
}: {
  icon: ComponentProps<typeof Ionicons>['name'];
  label: string;
  onPress: () => void;
}) {
  return (
    <Pressable onPress={onPress} accessibilityRole="button" style={styles.tile}>
      <Ionicons name={icon} size={26} color={colors.coral} />
      <Text style={styles.tileLabel}>{label}</Text>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1, backgroundColor: colors.cream },
  content: { padding: 16, gap: 14, paddingBottom: 32 },

  hero: {
    backgroundColor: colors.coral,
    borderRadius: radius.lg,
    padding: 22,
    gap: 10,
  },
  heroTitle: { color: colors.white, fontSize: 28, fontWeight: '800', lineHeight: 34 },
  heroSub: { color: colors.white, fontSize: 15, lineHeight: 21, opacity: 0.95 },
  heroButton: { marginTop: 6 },

  card: {
    backgroundColor: colors.white,
    borderRadius: radius.md,
    padding: 16,
    gap: 10,
    borderWidth: 1,
    borderColor: colors.line,
  },
  sectionLabel: { fontSize: 12, color: colors.inkSoft, fontWeight: '600', textTransform: 'uppercase' },
  cardTitle: { fontSize: 18, fontWeight: '700', color: colors.ink },

  sectionTitle: { fontSize: 18, fontWeight: '700', color: colors.ink, marginTop: 6 },

  actions: { flexDirection: 'row', gap: 10 },
  tile: {
    flex: 1,
    backgroundColor: colors.white,
    borderRadius: radius.md,
    paddingVertical: 16,
    alignItems: 'center',
    gap: 8,
    borderWidth: 1,
    borderColor: colors.line,
  },
  tileLabel: { fontSize: 13, fontWeight: '600', color: colors.ink, textAlign: 'center' },

  row: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
    backgroundColor: colors.white,
    borderRadius: radius.md,
    padding: 12,
    borderWidth: 1,
    borderColor: colors.line,
  },
  swatch: { width: 52, height: 52, borderRadius: radius.sm },
  rowTitle: { fontSize: 16, fontWeight: '700', color: colors.ink },
  rowSub: { fontSize: 13, color: colors.inkSoft, marginTop: 2 },
});
