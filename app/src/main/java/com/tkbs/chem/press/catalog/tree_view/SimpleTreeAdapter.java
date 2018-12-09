package com.tkbs.chem.press.catalog.tree_view;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.catalog.tree.bean.Node;
import com.tkbs.chem.press.catalog.tree.bean.TreeListViewAdapter;

import java.util.List;


public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T> {

    public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas,
                             int defaultExpandLevel) throws IllegalArgumentException,
            IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel);
    }


    @Override
    public View getConvertView(Node node, final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tb_catalog_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ly = (RelativeLayout) convertView
                    .findViewById(R.id.ly_headicon);
            viewHolder.icon = (ImageView) convertView
                    .findViewById(R.id.id_treenode_icon);
            viewHolder.headicon = (ImageView) convertView
                    .findViewById(R.id.id_treenode_headicon);
            viewHolder.label = (TextView) convertView
                    .findViewById(R.id.id_treenode_label);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (node.getIcon() == -1) {
            viewHolder.icon.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageResource(node.getIcon());
        }
        viewHolder.headicon.setImageResource(node.getHeadIcon());
        viewHolder.ly.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//				expandOrCollapse(position);

//				if (onTreeNodeClickListener != null)
//				{
//					onTreeNodeClickListener.onClick(mNodes.get(position),
//							position);
//				}
            }
        });
        viewHolder.label.setText(node.getName());


        return convertView;
    }

    private final class ViewHolder {
        RelativeLayout ly;
        ImageView icon;
        ImageView headicon;
        TextView label;
    }

}
