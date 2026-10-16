import { Pressable, StyleSheet, Text, View, type StyleProp, type ViewStyle } from 'react-native';

import { LEVEL_COLORS, type Level } from '../data/content';
import { colors, radius } from '../theme';

export function LevelBadge({ level }: { level: Level }) {
  return (
    <View style={[styles.badge, { backgroundColor: LEVEL_COLORS[level] + '26' }]}>
      <View style={[styles.badgeDot, { backgroundColor: LEVEL_COLORS[level] }]} />
      <Text style={[styles.badgeText, { color: colors.ink }]}>{level}</Text>
    </View>
  );
}

export function ProgressBar({ value, total }: { value: number; total: number }) {
  const pct = total === 0 ? 0 : Math.round((value / total) * 100);
  return (
    <View style={styles.progressWrap} accessibilityRole="progressbar" accessibilityValue={{ min: 0, max: 100, now: pct }}>
      <View style={styles.progressTrack}>
        <View style={[styles.progressFill, { width: `${pct}%` }]} />
      </View>
      <Text style={styles.progressText}>
        {value}/{total} done
      </Text>
    </View>
  );
}

export function Segmented<T extends string>({
  options,
  value,
  onChange,
}: {
  options: { label: string; value: T }[];
  value: T;
  onChange: (v: T) => void;
}) {
  return (
    <View style={styles.segmented}>
      {options.map((opt) => {
        const active = opt.value === value;
        return (
          <Pressable
            key={opt.value}
            onPress={() => onChange(opt.value)}
            accessibilityRole="button"
            accessibilityState={{ selected: active }}
            style={[styles.segment, active && styles.segmentActive]}
          >
            <Text style={[styles.segmentText, active && styles.segmentTextActive]}>{opt.label}</Text>
          </Pressable>
        );
      })}
    </View>
  );
}

export function Chip({
  label,
  active,
  onPress,
}: {
  label: string;
  active: boolean;
  onPress: () => void;
}) {
  return (
    <Pressable
      onPress={onPress}
      accessibilityRole="button"
      accessibilityState={{ selected: active }}
      style={[styles.chip, active && styles.chipActive]}
    >
      <Text style={[styles.chipText, active && styles.chipTextActive]}>{label}</Text>
    </Pressable>
  );
}

export function PrimaryButton({
  label,
  onPress,
  style,
  variant = 'solid',
}: {
  label: string;
  onPress: () => void;
  style?: StyleProp<ViewStyle>;
  variant?: 'solid' | 'light';
}) {
  const light = variant === 'light';
  return (
    <Pressable
      onPress={onPress}
      accessibilityRole="button"
      style={({ pressed }) => [
        styles.primaryButton,
        light && styles.primaryButtonLight,
        pressed && { opacity: 0.85 },
        style,
      ]}
    >
      <Text style={[styles.primaryButtonText, light && styles.primaryButtonTextLight]}>{label}</Text>
    </Pressable>
  );
}

export function EmptyState({ title, body }: { title: string; body: string }) {
  return (
    <View style={styles.empty}>
      <Text style={styles.emptyTitle}>{title}</Text>
      <Text style={styles.emptyBody}>{body}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  badge: {
    flexDirection: 'row',
    alignItems: 'center',
    alignSelf: 'flex-start',
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 999,
    gap: 6,
  },
  badgeDot: { width: 8, height: 8, borderRadius: 4 },
  badgeText: { fontSize: 12, fontWeight: '600' },

  progressWrap: { gap: 6 },
  progressTrack: {
    height: 8,
    borderRadius: 4,
    backgroundColor: colors.peachDeep,
    overflow: 'hidden',
  },
  progressFill: { height: 8, borderRadius: 4, backgroundColor: colors.coral },
  progressText: { fontSize: 12, color: colors.inkSoft },

  segmented: {
    flexDirection: 'row',
    backgroundColor: colors.white,
    borderRadius: radius.md,
    padding: 4,
    borderWidth: 1,
    borderColor: colors.line,
  },
  segment: {
    flex: 1,
    paddingVertical: 10,
    alignItems: 'center',
    borderRadius: radius.sm,
  },
  segmentActive: { backgroundColor: colors.coral },
  segmentText: { fontSize: 15, fontWeight: '600', color: colors.inkSoft },
  segmentTextActive: { color: colors.white },

  chip: {
    paddingHorizontal: 14,
    paddingVertical: 8,
    borderRadius: 999,
    backgroundColor: colors.white,
    borderWidth: 1,
    borderColor: colors.line,
  },
  chipActive: { backgroundColor: colors.ink, borderColor: colors.ink },
  chipText: { fontSize: 13, fontWeight: '600', color: colors.inkSoft },
  chipTextActive: { color: colors.white },

  primaryButton: {
    backgroundColor: colors.coral,
    borderRadius: radius.md,
    paddingVertical: 14,
    alignItems: 'center',
  },
  primaryButtonLight: { backgroundColor: colors.white },
  primaryButtonText: { color: colors.white, fontSize: 16, fontWeight: '700' },
  primaryButtonTextLight: { color: colors.coralDark },

  empty: {
    alignItems: 'center',
    padding: 24,
    gap: 6,
  },
  emptyTitle: { fontSize: 17, fontWeight: '700', color: colors.ink, textAlign: 'center' },
  emptyBody: { fontSize: 14, color: colors.inkSoft, textAlign: 'center', lineHeight: 20 },
});
