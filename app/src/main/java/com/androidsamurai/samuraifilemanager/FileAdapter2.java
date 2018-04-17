//package com.androidsamurai.samuraifilemanager;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.support.v4.content.ContextCompat;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.CheckBox;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.io.File;
//import java.io.FileFilter;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Locale;
//import java.util.Set;
//
//public class FileAdapter2 extends BaseAdapter {
//    private Context m_context;
//    private List<String> m_item;
//    private List<String> m_path;
//    public ArrayList<Integer> m_selectedItem;
//    private Boolean m_isRoot;
//    private File currentFolder;
//    private List<File> folders = new ArrayList<>();
//    private List<File> files = new ArrayList<>();
//    private Main activity;
//    private Set<String> selectedItems = new HashSet<>();
//    private Boolean homeFolder;
//    private byte offset;
//
//
////    public FileAdapter(Context m_context, List<String> m_item, List<String> m_path, Boolean m_isRoot) {
////        this.m_context = m_context;
////        this.m_item = m_item;
////        this.m_path = m_path;
////        m_selectedItem = new ArrayList<>();
////        this.m_isRoot = m_isRoot;
////    }
//
//    FileAdapter(File currentFolder, boolean homeFolder, Main activity) {
//        this.currentFolder = currentFolder;
//        this.homeFolder = homeFolder;
//        this.activity = activity;
//
//        File[] foldersArray = currentFolder.listFiles(new FileFilter() {
//            @Override
//            public boolean accept(File file) {
//                return file.isDirectory() && !file.getName().startsWith(".");
//            }
//        });
//
//        if (foldersArray != null) {
//            folders = new ArrayList<>(Arrays.asList(foldersArray));
//
//            File[] filesArray = currentFolder.listFiles(new FileFilter() {
//                @Override
//                public boolean accept(File file) {
//                    return !file.isDirectory() && !file.getName().startsWith(".");
//                }
//            });
//
//            if (filesArray != null) {
//                files = new ArrayList<>(Arrays.asList(filesArray));
//
//                Collections.sort(folders);
//                Collections.sort(files);
//            }
//        }
//
//    }
//
//    @Override
//    public int getCount() {
////        return m_item.size();
//        return folders.size() + files.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
////        return m_item.get(position);
//        return files.get(position - folders.size());
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//
////        View m_view = null;
////        ViewHolder m_viewHolder;
//
//        if (convertView == null) {
////            LayoutInflater layoutInflater = LayoutInflater.from(m_context);
//            LayoutInflater layoutInflater = (LayoutInflater) this.activity
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            convertView = layoutInflater.inflate(R.layout.file_item, parent, false);
////
////            m_view = layoutInflater.inflate(R.layout.files_list, null);
//////            m_viewHolder = new ViewHolder(convertView);
////            m_viewHolder = new ViewHolder();
////            m_viewHolder.m_tvFileName = (TextView) m_view.findViewById(R.id.lr_tvFileName);
////            m_viewHolder.m_tvDate = (TextView) m_view.findViewById(R.id.lr_tvdate);
////            m_viewHolder.m_ivIcon = (ImageView) m_view.findViewById(R.id.lr_ivFileIcon);
////            m_viewHolder.m_cbCheck = (CheckBox) m_view.findViewById(R.id.lr_cbCheck);
////            m_view.setTag(m_viewHolder);
//
////            convertView.setTag(m_viewHolder);
//
//        }
////        else {
////            m_viewHolder = (ViewHolder) convertView.getTag();
////        }
//
//        ImageView icon = convertView.findViewById(R.id.row_icon);
//        TextView name = convertView.findViewById(R.id.row_name);
//        TextView date = convertView.findViewById(R.id.row_date);
//
//        date.setText(getLastDate(position));
//
//        if (position < offset) {
//            if (position == 0 && !homeFolder) {
//                icon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.icon_home));
//                name.setText(activity.getResources().getString(R.string.home));
//                convertView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (activity.getActionMode() == null)
//                            activity.setDefaultFolder();
//                    }
//                });
//            } else {
//                icon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.icon_up));
//                name.setText(activity.getResources().getString(R.string.up));
//                convertView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (activity.getActionMode() == null)
//                            if (!currentFolder.getName().equals(""))
//                                activity.setCurrentFolder(currentFolder.getParentFile());
//                    }
//                });
//            }
//        } else {
//            File file;
//            if (position - offset < folders.size()) {
//                icon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.icon_folder));
//                file = folders.get(position - offset);
//            } else {
//                icon.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.icon_file));
//                file = files.get(position - offset - folders.size());
//            }
//            name.setText(file.getName());
//
//            if (selectedItems.contains(file.getName())) {
//                convertView.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary));
//                name.setTextColor(ContextCompat.getColor(activity, R.color.white));
//            } else {
//                convertView.setBackgroundColor(Color.TRANSPARENT);
//                name.setTextColor(ContextCompat.getColor(activity, R.color.text));
//            }
//        }
//
//
////        if (!m_isRoot && position == 0) {
////            m_viewHolder.m_cbCheck.setVisibility(View.INVISIBLE);
////        }
////
////        m_viewHolder.m_tvFileName.setText(m_item.get(position));
////        m_viewHolder.m_ivIcon.setImageResource(setFileImageType(new File(m_path.get(position))));
////        m_viewHolder.m_tvDate.setText(getLastDate(position));
////        m_viewHolder.m_cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                if (isChecked) {
////                    m_selectedItem.add(position);
////                } else {
////                    m_selectedItem.remove(m_selectedItem.indexOf(position));
////                }
////            }
////        });
//        return convertView;
//    }
//
//    private class ViewHolder {
//        CheckBox m_cbCheck;
//        ImageView m_ivIcon;
//        TextView m_tvFileName;
//        TextView m_tvDate;
//
////        ViewHolder(View view) {
////            m_cbCheck = view.findViewById(R.id.lr_cbCheck);
////            m_ivIcon = view.findViewById(R.id.lr_ivFileIcon);
////            m_tvFileName = view.findViewById(R.id.lr_tvFileName);
////            m_tvDate = view.findViewById(R.id.lr_tvdate);
////        }
//    }
//
//    private int setFileImageType(File m_file) {
//        int m_lastIndex = m_file.getAbsolutePath().lastIndexOf(".");
//        String m_filepath = m_file.getAbsolutePath();
//
//        if (m_file.isDirectory()) {
//            return R.drawable.icon_home;
//        } else {
////            if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".png")) {
////                return R.drawable.icon_file;
////            } else if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".jpg")) {
////                return R.drawable.icon_file;
////            } else {
////                return R.drawable.icon_file;
////            }
//            return R.drawable.icon_file;
//
//        }
//    }
//
//    private String getLastDate(int position) {
//        File m_file = new File(m_path.get(position));
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
//        return dateFormat.format(m_file.lastModified());
//    }
//
//    /**
//     * Called when item is selected in Action mode
//     *
//     * @param position clicked file position in list
//     */
//    void itemSelect(int position) {
//        boolean isSelected = false;
//        for (String fileName : selectedItems) {
//            if (fileName.equals(((File) getItem(position)).getName())) {
//                isSelected = true;
//                break;
//            }
//        }
//        if (!isSelected)
//            selectedItems.add(((File) getItem(position)).getName());
//        else
//            selectedItems.remove(((File) getItem(position)).getName());
//
//        notifyDataSetChanged();
//    }
//
//    /**
//     * Removes all selected files (-> when setting Action mode off)
//     */
//    void removeSelection(){
//        selectedItems = new HashSet<>();
//        notifyDataSetChanged();
//    }
//
//    /**
//     * Returns files selected in Action mode
//     * @return selected files set
//     */
//    Set<String> getSelectedItems(){
//        return selectedItems;
//    }
//
//    /**
//     * Returns selected files count
//     * @return selected files count
//     */
//    int getSelectedCount(){
//        return selectedItems.size();
//    }
//}
