package com.androidsamurai.samuraifilemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FileAdapter extends BaseAdapter {
    private Context m_context;
    private List<String> m_item;
    private List<String> m_path;
    public ArrayList<Integer> m_selectedItem;
    private Boolean m_isRoot;

    public FileAdapter(Context m_context, List<String> m_item, List<String> m_path, Boolean m_isRoot) {
        this.m_context = m_context;
        this.m_item = m_item;
        this.m_path = m_path;
        m_selectedItem = new ArrayList<>();
        this.m_isRoot = m_isRoot;
    }

    @Override
    public int getCount() {
        return m_item.size();
    }

    @Override
    public Object getItem(int position) {
        return m_item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View m_view = null;
        ViewHolder m_viewHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(m_context);
            m_view = layoutInflater.inflate(R.layout.files_list, null);
            m_viewHolder = new ViewHolder(convertView);
            convertView.setTag(m_viewHolder);

        } else {
            m_viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!m_isRoot && position == 0) {
            m_viewHolder.m_cbCheck.setVisibility(View.INVISIBLE);
        }

        m_viewHolder.m_tvFileName.setText(m_item.get(position));
        m_viewHolder.m_ivIcon.setImageResource(setFileImageType(new File(m_path.get(position))));
        m_viewHolder.m_tvDate.setText(getLastDate(position));
        m_viewHolder.m_cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    m_selectedItem.add(position);
                } else {
                    m_selectedItem.remove(m_selectedItem.indexOf(position));
                }
            }
        });
        return m_view;
    }

    private class ViewHolder {
        CheckBox m_cbCheck;
        ImageView m_ivIcon;
        TextView m_tvFileName;
        TextView m_tvDate;

        ViewHolder(View view) {
            m_cbCheck = view.findViewById(R.id.lr_cbCheck);
            m_ivIcon = view.findViewById(R.id.lr_ivFileIcon);
            m_tvFileName = view.findViewById(R.id.lr_tvFileName);
            m_tvDate = view.findViewById(R.id.lr_tvdate);
        }
    }

    private int setFileImageType(File m_file) {
        int m_lastIndex = m_file.getAbsolutePath().lastIndexOf(".");
        String m_filepath = m_file.getAbsolutePath();

        if (m_file.isDirectory()) {
            return R.drawable.icon_home;
        } else {
            if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".png")) {
                return R.drawable.icon_file;
            } else if (m_filepath.substring(m_lastIndex).equalsIgnoreCase(".jpg")) {
                return R.drawable.icon_file;
            } else {
                return R.drawable.icon_file;
            }
        }
    }

    private String getLastDate(int position) {
        File m_file = new File(m_path.get(position));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
        return dateFormat.format(m_file.lastModified());
    }
}
