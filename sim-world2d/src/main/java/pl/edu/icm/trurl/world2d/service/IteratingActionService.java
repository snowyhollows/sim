package pl.edu.icm.trurl.world2d.service;

import net.snowyhollows.bento.annotation.WithFactory;
import pl.edu.icm.trurl.ecs.Entity;
import pl.edu.icm.trurl.ecs.Session;
import pl.edu.icm.trurl.ecs.index.ChunkInfo;
import pl.edu.icm.trurl.ecs.util.ContextualAction;
import pl.edu.icm.trurl.store.reference.Reference;

public class IteratingActionService {

    @WithFactory
    public IteratingActionService() {
    }

    public <T> void run(ContextualAction<T> action, Entity entity, Reference reference) {
        Session session = entity.getSession();
        T context = action.initPrivateContext(session, ChunkInfo.NO_CHUNK);
        int row = entity.getId();
        for (int i = 0;; i++) {
            int id = reference.getId(row, i);
            if (id == Integer.MIN_VALUE) {
                break;
            }
            action.perform(context, session, id);
        }
        action.closePrivateContext(context);
    }
}
