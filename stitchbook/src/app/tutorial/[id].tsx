import { Stack, useLocalSearchParams } from 'expo-router';
import { ScrollView, StyleSheet, Text, View } from 'react-native';

import { EmptyState, LevelBadge } from '../../components/ui';
import { getTutorial } from '../../data/content';
import { colors, radius } from '../../theme';

export default function TutorialScreen() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const tutorial = getTutorial(id);

  if (!tutorial) {
    return <EmptyState title="Tutorial not found" body="This lesson may have moved." />;
  }

  return (
    <>
      <Stack.Screen options={{ title: tutorial.title }} />
      <ScrollView contentContainerStyle={styles.content}>
        <View style={[styles.hero, { backgroundColor: tutorial.color }]}>
          <Text style={styles.craft}>{tutorial.craft === 'knitting' ? 'Knitting' : 'Crochet'}</Text>
          <Text style={styles.title}>{tutorial.title}</Text>
        </View>

        <View style={styles.meta}>
          <LevelBadge level={tutorial.level} />
          <Text style={styles.minutes}>{tutorial.minutes} min</Text>
        </View>
        <Text style={styles.summary}>{tutorial.summary}</Text>

        <Text style={styles.sectionTitle}>Step by step</Text>
        {tutorial.steps.map((step, i) => (
          <View key={step.title} style={styles.step}>
            <View style={styles.stepNumber}>
              <Text style={styles.stepNumberText}>{i + 1}</Text>
            </View>
            <View style={{ flex: 1, gap: 4 }}>
              <Text style={styles.stepTitle}>{step.title}</Text>
              <Text style={styles.stepText}>{step.text}</Text>
            </View>
          </View>
        ))}
      </ScrollView>
    </>
  );
}

const styles = StyleSheet.create({
  content: { padding: 16, gap: 14, paddingBottom: 40, backgroundColor: colors.cream },
  hero: { borderRadius: radius.lg, padding: 22, minHeight: 130, justifyContent: 'flex-end', gap: 6 },
  craft: { color: colors.white, fontSize: 12, fontWeight: '700', textTransform: 'uppercase', opacity: 0.9 },
  title: { color: colors.white, fontSize: 26, fontWeight: '800' },
  meta: { flexDirection: 'row', alignItems: 'center', gap: 12 },
  minutes: { fontSize: 13, color: colors.inkSoft },
  summary: { fontSize: 15, color: colors.ink, lineHeight: 22 },
  sectionTitle: { fontSize: 18, fontWeight: '700', color: colors.ink, marginTop: 6 },
  step: {
    flexDirection: 'row',
    gap: 12,
    backgroundColor: colors.white,
    borderRadius: radius.md,
    padding: 14,
    borderWidth: 1,
    borderColor: colors.line,
  },
  stepNumber: {
    width: 30,
    height: 30,
    borderRadius: 15,
    backgroundColor: colors.peach,
    alignItems: 'center',
    justifyContent: 'center',
  },
  stepNumberText: { fontWeight: '800', color: colors.coralDark },
  stepTitle: { fontSize: 15, fontWeight: '700', color: colors.ink },
  stepText: { fontSize: 14, color: colors.inkSoft, lineHeight: 20 },
});
