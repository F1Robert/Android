# Android Recycle view的adapter根据参数绑定不同的布局视图

```
//如下所示的代码中，是adapter绑定子元素视图的代码，通过重写以下方法可以判断修改所要绑定的子元素视图
@Override
    public int getItemViewType(int position) {
        if (isStatusNotificationHeader(position)) {
            return R.layout.conversationlist_item_notification_container;
        }
        Conversation conversation = conversationInfos.get(position - headerCount()).conversation;
        return conversation.type.getValue() << 24 | conversation.line;
    }

public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == R.layout.conversationlist_item_notification_container) {
        View view = LayoutInflater.from(fragment.getContext()).inflate(R.layout.conversationlist_item_notification_container, parent, false);
        return new StatusNotificationContainerViewHolder(view);
    }
    Class<? extends ConversationViewHolder> viewHolderClazz = 		ConversationViewHolderManager.getInstance().getConversationContentViewHolder(viewType);
    View itemView;
    itemView = LayoutInflater.from(fragment.getContext()).inflate(R.layout.conversationlist_item_conversation, parent, false);

    try {
        Constructor constructor = viewHolderClazz.getConstructor(Fragment.class, RecyclerView.Adapter.class, View.class);
        ConversationViewHolder viewHolder = (ConversationViewHolder) constructor.newInstance(fragment, this, itemView);
        processConversationClick(viewHolder, itemView);
        processConversationLongClick(viewHolderClazz, viewHolder, itemView);
        return viewHolder;
    } catch (NoSuchMethodException e) {
        e.printStackTrace();
    } catch (IllegalAccessException e) {
        e.printStackTrace();
    } catch (InstantiationException e) {
        e.printStackTrace();
    } catch (InvocationTargetException e) {
        e.printStackTrace();
    }
    return null;
}
```