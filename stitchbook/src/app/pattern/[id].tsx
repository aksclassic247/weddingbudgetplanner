import { Stack, router, useLocalSearchParams } from 'expo-router';
import { ScrollView, StyleSheet, Text, View } from 'react-native';

import { EmptyState, LevelBadge, PrimaryButton } from '../../components/ui';
import { useProjects } from '../../context/ProjectsContext';
import { getPattern } from '../../data/content';
import { colors, radius } from '../../theme';

export default function PatternScreen() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const pattern = getPattern(id);
  const { startProject } = useProjects();

  if (!pattern) {
    return <EmptyState title="Pattern not found" body="Try browsing the patterns list again." />;
  }

  function onStart() {
    const projectId = startProject(pattern!.id);
    if (projectId) router.push(`/project/${projectId}`);
  }

  return (
    <>
      <Stack.Screen options={{ title: pattern.title }} />
      <ScrollView contentContainerStyle={styles.content}>
        <View style={[styles.cover, { backgroundColor: pattern.color }]}>
          <Text style={styles.emoji}>{pattern.emoji}</Text>
        </View>

        <Text style={styles.title}>{pattern.title}</Text>
        <View style={styles.meta}>
          <LevelBadge level={pattern.level} />
          <Text style={styles.metaText}>{pattern.craft === 'knitting' ? 'Knitting' : 'Crochet'}</Text>
          <Text style={styles.metaText}>{pattern.time}</Text>
        </View>

        <View style={styles.card}>
          <Text style={styles.cardTitle}>Materials</Text>
          {pattern.materials.map((m) => (
            <Text key={m} style={styles.bullet}>
              • {m}
            </Text>
          ))}
          {pattern.gauge ? <Text style={styles.gauge}>Gauge: {pattern.gauge}</Text> : null}
        </View>

        <Text style={styles.sectionTitle}>Instructions</Text>
        {pattern.steps.map((s, i) => (
          <Text key={i} style={styles.step}>
            {s}
          </Text>
        ))}

        <PrimaryButton label="Start this project" onPress={onStart} style={styles.start} />
        <Text style={styles.note}>These are sample instructions. Check the details against your yarn and hook or needles before you begin.</Text>
      </ScrollView>
    </>
  );
}

const styles = StyleSheet.create({
  content: { padding: 16, gap: 14, paddingBottom: 40, backgroundColor: colors.cream },
  cover: { height: 170, borderRadius: radius.lg, alignItems: 'center', justifyContent: 'center' },
  emoji: { fontSize: 72 },
  title: { fontSize: 26, fontWeight: '800', color: colors.ink },
  meta: { flexDirection: 'row', alignItems: 'center', gap: 12, flexWrap: 'wrap' },
  metaText: { fontSize: 13, color: colors.inkSoft },
  card: {
    backgroundColor: colors.white,
    borderRadius: radius.md,
    padding: 16,
    gap: 6,
    borderWidth: 1,
    borderColor: colors.line,
  },
  cardTitle: { fontSize: 16, fontWeight: '700', color: colors.ink, marginBottom: 2 },
  bullet: { fontSize: 14, color: colors.ink, lineHeight: 20 },
  gauge: { fontSize: 13, color: colors.inkSoft, marginTop: 6 },
  sectionTitle: { fontSize: 18, fontWeight: '700', color: colors.ink, marginTop: 6 },
  step: {
    fontSize: 14,
    color: colors.ink,
    lineHeight: 21,
    backgroundColor: colors.white,
    borderRadius: radius.sm,
    padding: 12,
    borderWidth: 1,
    borderColor: colors.line,
  },
  start: { marginTop: 10 },
  note: { fontSize: 12, color: colors.inkSoft, textAlign: 'center', lineHeight: 17 },
});
