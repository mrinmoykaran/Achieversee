package com.example.edukit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MockQuestionAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<MockQuestionModel> questinList;

    public MockQuestionAdapter(Context context, List<MockQuestionModel> courseList) {
        this.context = context;
        this.questinList = courseList;

    }

    @Override
    public int getItemViewType(int position) {
        if (String.valueOf(questinList.get(position).getQ_type()).equals("1")) {
            return 1;
        } else if (String.valueOf(questinList.get(position).getQ_type()).equals("2")) {
            return 2;
        } else if (String.valueOf(questinList.get(position).getQ_type()).equals("3")) {
            return 3;
        }
        return 4;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view;
        if (i == 1) {
            //Toast.makeText(context, i+"", Toast.LENGTH_SHORT).show();
            view = layoutInflater.inflate(R.layout.qview_1, viewGroup, false);
            return new ViewHolder_1(view);
        } else if (i == 2) {
            // Toast.makeText(context, i+"", Toast.LENGTH_SHORT).show();
            view = layoutInflater.inflate(R.layout.qview_2, viewGroup, false);
            return new ViewHolder_2(view);
        } else if (i == 3) {
            //Toast.makeText(context, i+"", Toast.LENGTH_SHORT).show();
            view = layoutInflater.inflate(R.layout.qview_3, viewGroup, false);
            return new ViewHolder_3(view);
        }
        view = layoutInflater.inflate(R.layout.qview_4, viewGroup, false);
        return new ViewHolder_4(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

        if (String.valueOf(questinList.get(position).getQ_type()).equals("1")) {

            ViewHolder_1 holder1 = (ViewHolder_1) holder;
            holder1.qNo.setText(String.valueOf(position + 1));
            holder1.qTitle.setText(String.valueOf(questinList.get(position).getQestion()));
            holder1.opt1.setText("a) " + String.valueOf(questinList.get(position).getOption1()));
            holder1.opt2.setText("b) " + String.valueOf(questinList.get(position).getOption2()));
            holder1.opt3.setText("c) " + String.valueOf(questinList.get(position).getOption3()));
            holder1.opt4.setText("d) " + String.valueOf(questinList.get(position).getOption4()));
            holder1.setOptions(position);
        } else if (String.valueOf(questinList.get(position).getQ_type()).equals("2")) {
            ViewHolder_2 holder1 = (ViewHolder_2) holder;
            holder1.qNo.setText(String.valueOf(position + 1));
            Picasso.with(context).load(String.valueOf(questinList.get(position).getQestion()).trim()).into(holder1.qTitle);
            Picasso.with(context).load(String.valueOf(questinList.get(position).getOption1()).trim()).into(holder1.opt1);
            Picasso.with(context).load(String.valueOf(questinList.get(position).getOption2()).trim()).into(holder1.opt2);
            Picasso.with(context).load(String.valueOf(questinList.get(position).getOption3()).trim()).into(holder1.opt3);
            Picasso.with(context).load(String.valueOf(questinList.get(position).getOption4()).trim()).into(holder1.opt4);
            holder1.setOptions(position);
        } else if (String.valueOf(questinList.get(position).getQ_type()).equals("3")) {
            ViewHolder_3 holder1 = (ViewHolder_3) holder;
            holder1.qNo.setText(String.valueOf(position + 1));
            Picasso.with(context).load(String.valueOf(questinList.get(position).getQestion()).trim()).into(holder1.qTitle);
            holder1.opt1.setText("a) " + String.valueOf(questinList.get(position).getOption1()));
            holder1.opt2.setText("b) " + String.valueOf(questinList.get(position).getOption2()));
            holder1.opt3.setText("c) " + String.valueOf(questinList.get(position).getOption3()));
            holder1.opt4.setText("d) " + String.valueOf(questinList.get(position).getOption4()));
            holder1.setOptions(position);
        } else if (String.valueOf(questinList.get(position).getQ_type()).equals("4")) {
            ViewHolder_4 holder1 = (ViewHolder_4) holder;
            holder1.qNo.setText(String.valueOf(position + 1));
            holder1.qTitle.setText(String.valueOf(questinList.get(position).getQestion()));
            Picasso.with(context).load(String.valueOf(questinList.get(position).getOption1()).trim()).into(holder1.opt1);
            Picasso.with(context).load(String.valueOf(questinList.get(position).getOption2()).trim()).into(holder1.opt2);
            Picasso.with(context).load(String.valueOf(questinList.get(position).getOption3()).trim()).into(holder1.opt3);
            Picasso.with(context).load(String.valueOf(questinList.get(position).getOption4()).trim()).into(holder1.opt4);
            holder1.setOptions(position);
        }

    }

    @Override
    public int getItemCount() {
        return questinList.size();
    }

    class ViewHolder_1 extends RecyclerView.ViewHolder {
        TextView qNo, qTitle;
        RadioButton opt1, opt2, opt3, opt4;
        RadioGroup rgResult1;

        public ViewHolder_1(@NonNull View itemView) {
            super(itemView);
            qNo = itemView.findViewById(R.id.qNo);
            qTitle = itemView.findViewById(R.id.qTitle);
            opt1 = itemView.findViewById(R.id.opt1);
            opt2 = itemView.findViewById(R.id.opt2);
            opt3 = itemView.findViewById(R.id.opt3);
            opt4 = itemView.findViewById(R.id.opt4);
            rgResult1 = itemView.findViewById(R.id.rgOptions);
            //   rgResult1.setTag(getAdapterPosition());

        }

        public void setOptions(final int mPosition) {
            if (questinList.get(mPosition).isAnswerd) {
                rgResult1.check(questinList.get(mPosition).chekedId);
            } else {
                rgResult1.check(-1);
                // Toast.makeText(context, "Checked", Toast.LENGTH_SHORT).show();
            }
            rgResult1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int position = getAdapterPosition();
                    questinList.get(position).isAnswerd = true;
                    questinList.get(position).chekedId = checkedId;
                    if (checkedId != -1) {
                        RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                        boolean isChecked = checkedRadioButton.isChecked();
                        if (isChecked) {
                            questinList.get(position).userAnswer = String.valueOf(checkedRadioButton.getTag());
                        }
                    }
                }
            });
        }

    }

    class ViewHolder_2 extends RecyclerView.ViewHolder {
        TextView qNo;
        ImageView qTitle, opt1, opt2, opt3, opt4;
        RadioGroup rgResult1;

        public ViewHolder_2(@NonNull View itemView) {
            super(itemView);
            qNo = itemView.findViewById(R.id.qNo);
            qTitle = itemView.findViewById(R.id.qTitle);
            opt1 = itemView.findViewById(R.id.opt1);
            opt2 = itemView.findViewById(R.id.opt2);
            opt3 = itemView.findViewById(R.id.opt3);
            opt4 = itemView.findViewById(R.id.opt4);
            rgResult1 = itemView.findViewById(R.id.rgOptions);
            // rgResult1.setTag(getAdapterPosition());
        }

        public void setOptions(int mPosition) {
            if (questinList.get(mPosition).isAnswerd) {
                rgResult1.check(questinList.get(mPosition).chekedId);
            } else {
                try {
                    rgResult1.check(-1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            rgResult1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int position = getAdapterPosition();
                    questinList.get(position).isAnswerd = true;
                    questinList.get(position).chekedId = checkedId;
                    if (checkedId != -1) {
                        RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                        boolean isChecked = checkedRadioButton.isChecked();
                        if (isChecked) {
                            questinList.get(position).userAnswer = String.valueOf(checkedRadioButton.getTag());
                            // Toast.makeText(context,questinList.get(position).userAnswer, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    class ViewHolder_3 extends RecyclerView.ViewHolder {
        TextView qNo;
        ImageView qTitle;
        RadioButton opt1, opt2, opt3, opt4;
        RadioGroup rgResult1;

        public ViewHolder_3(@NonNull View itemView) {
            super(itemView);
            qNo = itemView.findViewById(R.id.qNo);
            qTitle = itemView.findViewById(R.id.qTitle);
            opt1 = itemView.findViewById(R.id.opt1);
            opt2 = itemView.findViewById(R.id.opt2);
            opt3 = itemView.findViewById(R.id.opt3);
            opt4 = itemView.findViewById(R.id.opt4);
            rgResult1 = itemView.findViewById(R.id.rgOptions);
            //   rgResult1.setTag(getAdapterPosition());
        }

        public void setOptions(int mPosition) {
            if (questinList.get(mPosition).isAnswerd) {
                rgResult1.check(questinList.get(mPosition).chekedId);
            } else {
                rgResult1.check(-1);
            }
            rgResult1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int position = getAdapterPosition();
                    questinList.get(position).isAnswerd = true;
                    questinList.get(position).chekedId = checkedId;
                    if (checkedId != -1) {
                        RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                        boolean isChecked = checkedRadioButton.isChecked();
                        if (isChecked) {
                            questinList.get(position).userAnswer = String.valueOf(checkedRadioButton.getTag());
                            // Toast.makeText(context,questinList.get(position).userAnswer, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    class ViewHolder_4 extends RecyclerView.ViewHolder {
        TextView qNo, qTitle;
        ImageView opt1, opt2, opt3, opt4;
        RadioGroup rgResult1;

        public ViewHolder_4(@NonNull View itemView) {
            super(itemView);
            qNo = itemView.findViewById(R.id.qNo);
            qTitle = itemView.findViewById(R.id.qTitle);
            opt1 = itemView.findViewById(R.id.opt1);
            opt2 = itemView.findViewById(R.id.opt2);
            opt3 = itemView.findViewById(R.id.opt3);
            opt4 = itemView.findViewById(R.id.opt4);
            rgResult1 = itemView.findViewById(R.id.rgOptions);
            // rgResult1.setTag(getAdapterPosition());
        }

        public void setOptions(int mPosition) {
            if (questinList.get(mPosition).isAnswerd) {
                rgResult1.check(questinList.get(mPosition).chekedId);
            } else {
                rgResult1.check(-1);
            }
            rgResult1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int position = getAdapterPosition();
                    questinList.get(position).isAnswerd = true;
                    questinList.get(position).chekedId = checkedId;
                    if (checkedId != -1) {
                        RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                        boolean isChecked = checkedRadioButton.isChecked();
                        if (isChecked) {
                            questinList.get(position).userAnswer = String.valueOf(checkedRadioButton.getTag());
                            // Toast.makeText(context,questinList.get(position).userAnswer, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}
