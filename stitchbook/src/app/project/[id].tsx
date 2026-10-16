import { Stack, router, useLocalSearchParams } from 'expo-router';
import { Alert, Pressable, ScrollView, StyleSheet, Text, View } from 'react-native';

import { EmptyState, ProgressBar } from '../../components/ui';
import { useProjects } from '../../context/ProjectsContext';
import { getPattern } from '../../data/content';
import { colors, radius } from '../../theme';

export default function ProjectScreen() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const { projects, toggleStep, removeProject } = useProjects();
  const project = projects.find((p) => p.id === id);
  const pattern = project ? getPattern(project.patternId) : undefined;

  if (!project || !pattern) {
    return <EmptyState title="Project not found" body="It may have been removed." />;
  }

  const done = project.done.filter(Boolean).length;

  function confirmRemove() {
    Alert.alert('Remove project?', 'Your progress on this project will be deleted.', [
      { text: 'Cancel', style: 'cancel' },
      {
        text: 'Remove',
        style: 'destructive',
        onPress: () => {
          removeProject(project!.id);
          router.back();
        },
      },
    ]);
  }

  return (
    <>
      <Stack.Screen options={{ title: pattern.title }} />
      <ScrollView contentContainerStyle={styles.content}>
        <View style={[styles.hero, { backgroundColor: pattern.color }]}>
          <Text style={styles.emoji}>{pattern.emoji}</Text>
          <Text style={styles.title}>{pattern.title}</Text>
        </View>

        <View style={styles.card}>
          <ProgressBar value={done} total={project.done.length} />
          {done === project.done.length ? <Text style={styles.finished}>All steps done. Nice work!</Text> : null}
        </View>

        <Text style={styles.sectionTitle}>Checklist</Text>
        {pattern.steps.map((step, i) => {
          const checked = project.done[i];
          return (
            <Pressable
              key={i}
              onPress={() => toggleStep(project.id, i)}
              accessibilityRole="checkbox"
              accessibilityState={{ checked }}
              style={[styles.step, checked && styles.stepDone]}
            >
              <View style={[styles.box, checked && styles.boxChecked]}>
                {checked ? <Text style={styles.tick}>✓</Text> : null}
              </View>
              <Text style={[styles.stepText, checked && styles.stepTextDone]}>{step}</Text>
            </Pressable>
          );
        })}

        <Pressable onPress={confirmRemove} accessibilityRole="button" style={styles.remove}>
          <Text style={styles.removeText}>Remove project</Text>
        </Pressable>
      </ScrollView>
    </>
  );
}

const styles = StyleSheet.create({
  content: { padding: 16, gap: 14, paddingBottom: 40, backgroundColor: colors.cream },
  hero: { borderRadius: radius.lg, padding: 20, alignItems: 'center', gap: 6 },
  emoji: { fontSize: 48 },
  title: { fontSize: 22, fontWeight: '800', color: colors.ink },
  card: {
    backgroundColor: colors.white,
    borderRadius: radius.md,
    padding: 16,
    gap: 10,
    borderWidth: 1,
    borderColor: colors.line,
  },
  finished: { fontSize: 13, fontWeight: '700', color: colors.success },
  sectionTitle: { fontSize: 18, fontWeight: '700', color: colors.ink, marginTop: 6 },
  step: {
    flexDirection: 'row',
    gap: 12,
    alignItems: 'flex-start',
    backgroundColor: colors.white,
    borderRadius: radius.md,
    padding: 14,
    borderWidth: 1,
    borderColor: colors.line,
  },
  stepDone: { backgroundColor: colors.peach, borderColor: colors.peachDeep },
  box: {
    width: 24,
    height: 24,
    borderRadius: 6,
    borderWidth: 2,
    borderColor: colors.coral,
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: 1,
  },
  boxChecked: { backgroundColor: colors.coral },
  tick: { color: colors.white, fontWeight: '800', fontSize: 14 },
  stepText: { flex: 1, fontSize: 14, color: colors.ink, lineHeight: 21 },
  stepTextDone: { color: colors.inkSoft, textDecorationLine: 'line-through' },
  remove: { alignItems: 'center', paddingVertical: 14, marginTop: 10 },
  removeText: { color: colors.coralDark, fontWeight: '600', fontSize: 15 },
});
