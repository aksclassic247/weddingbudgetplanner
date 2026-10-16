import AsyncStorage from '@react-native-async-storage/async-storage';
import { createContext, useCallback, useContext, useEffect, useMemo, useState, type ReactNode } from 'react';

import { getPattern } from '../data/content';

export type Project = {
  id: string;
  patternId: string;
  startedAt: number;
  // One boolean per pattern step, true when that step is finished.
  done: boolean[];
};

type ProjectsContextValue = {
  projects: Project[];
  loaded: boolean;
  startProject: (patternId: string) => string | null;
  toggleStep: (projectId: string, index: number) => void;
  removeProject: (projectId: string) => void;
};

const STORAGE_KEY = 'stitchbook.projects.v1';

const ProjectsContext = createContext<ProjectsContextValue | null>(null);

export function ProjectsProvider({ children }: { children: ReactNode }) {
  const [projects, setProjects] = useState<Project[]>([]);
  const [loaded, setLoaded] = useState(false);

  // Load saved projects once on launch. If storage fails, the app still works with an empty list.
  useEffect(() => {
    let cancelled = false;
    (async () => {
      try {
        const raw = await AsyncStorage.getItem(STORAGE_KEY);
        if (!cancelled && raw) {
          setProjects(JSON.parse(raw) as Project[]);
        }
      } catch {
        // Ignore a corrupt or unreadable save and start fresh.
      } finally {
        if (!cancelled) setLoaded(true);
      }
    })();
    return () => {
      cancelled = true;
    };
  }, []);

  // Save every change. Failures are ignored so the UI never blocks on storage.
  useEffect(() => {
    if (!loaded) return;
    AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(projects)).catch(() => {});
  }, [projects, loaded]);

  const startProject = useCallback((patternId: string) => {
    const pattern = getPattern(patternId);
    if (!pattern) return null;

    const project: Project = {
      id: `${patternId}-${Date.now()}`,
      patternId,
      startedAt: Date.now(),
      done: pattern.steps.map(() => false),
    };
    setProjects((prev) => [project, ...prev]);
    return project.id;
  }, []);

  const toggleStep = useCallback((projectId: string, index: number) => {
    setProjects((prev) =>
      prev.map((p) => {
        if (p.id !== projectId) return p;
        const done = p.done.slice();
        done[index] = !done[index];
        return { ...p, done };
      }),
    );
  }, []);

  const removeProject = useCallback((projectId: string) => {
    setProjects((prev) => prev.filter((p) => p.id !== projectId));
  }, []);

  const value = useMemo(
    () => ({ projects, loaded, startProject, toggleStep, removeProject }),
    [projects, loaded, startProject, toggleStep, removeProject],
  );

  return <ProjectsContext.Provider value={value}>{children}</ProjectsContext.Provider>;
}

export function useProjects() {
  const ctx = useContext(ProjectsContext);
  if (!ctx) throw new Error('useProjects must be used inside ProjectsProvider');
  return ctx;
}
