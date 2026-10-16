import { Ionicons } from '@expo/vector-icons';
import { router } from 'expo-router';
import { FlatList, Pressable, StyleSheet, Text, View } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

import { EmptyState, PrimaryButton, ProgressBar } from '../../components/ui';
import { type Project, useProjects } from '../../context/ProjectsContext';
import { getPattern } from '../../data/content';
import { colors, radius } from '../../theme';

export default function ProjectsScreen() {
  const { projects, loaded } = useProjects();

  return (
    <SafeAreaView style={styles.safe} edges={['top']}>
      <View style={styles.header}>
        <Text style={styles.heading}>My projects</Text>
      </View>

      <FlatList
        data={projects}
        keyExtractor={(p) => p.id}
        contentContainerStyle={styles.list}
        ItemSeparatorComponent={() => <View style={{ height: 12 }} />}
        ListEmptyComponent={
          loaded ? (
            <View style={styles.emptyWrap}>
              <EmptyState
                title="No projects yet"
                body="Pick a pattern and start a project. Your progress is saved on this device."
              />
              <PrimaryButton label="Browse patterns" onPress={() => router.push('/patterns')} />
            </View>
          ) : undefined
        }
        renderItem={({ item }) => <ProjectRow project={item} />}
      />
    </SafeAreaView>
  );
}

function ProjectRow({ project }: { project: Project }) {
  const pattern = getPattern(project.patternId);
  if (!pattern) return null;
  const done = project.done.filter(Boolean).length;
  const finished = done === project.done.length;

  return (
    <Pressable
      onPress={() => router.push(`/project/${project.id}`)}
      accessibilityRole="button"
      style={styles.card}
    >
      <View style={[styles.cover, { backgroundColor: pattern.color }]}>
        <Text style={styles.emoji}>{pattern.emoji}</Text>
      </View>
      <View style={styles.cardBody}>
        <Text style={styles.cardTitle}>{pattern.title}</Text>
        <ProgressBar value={done} total={project.done.length} />
        {finished ? <Text style={styles.finished}>Finished</Text> : null}
      </View>
      <Ionicons name="chevron-forward" size={18} color={colors.inkSoft} />
    </Pressable>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1, backgroundColor: colors.cream },
  header: { paddingHorizontal: 16, paddingTop: 8, paddingBottom: 8 },
  heading: { fontSize: 28, fontWeight: '800', color: colors.ink },
  list: { padding: 16, paddingBottom: 32, flexGrow: 1 },
  emptyWrap: { gap: 16, paddingTop: 40 },

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
  cover: { width: 64, height: 64, borderRadius: radius.sm, alignItems: 'center', justifyContent: 'center' },
  emoji: { fontSize: 30 },
  cardBody: { flex: 1, gap: 8 },
  cardTitle: { fontSize: 16, fontWeight: '700', color: colors.ink },
  finished: { fontSize: 12, fontWeight: '700', color: colors.success },
});
