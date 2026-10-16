import { Stack } from 'expo-router';
import { StatusBar } from 'expo-status-bar';

import { ProjectsProvider } from '../context/ProjectsContext';
import { colors } from '../theme';

export default function RootLayout() {
  return (
    <ProjectsProvider>
      <StatusBar style="dark" />
      <Stack
        screenOptions={{
          headerTintColor: colors.ink,
          headerStyle: { backgroundColor: colors.cream },
          headerShadowVisible: false,
          contentStyle: { backgroundColor: colors.cream },
        }}
      >
        <Stack.Screen name="(tabs)" options={{ headerShown: false }} />
        <Stack.Screen name="tutorial/[id]" options={{ title: 'Tutorial' }} />
        <Stack.Screen name="pattern/[id]" options={{ title: 'Pattern' }} />
        <Stack.Screen name="project/[id]" options={{ title: 'Project' }} />
      </Stack>
    </ProjectsProvider>
  );
}
